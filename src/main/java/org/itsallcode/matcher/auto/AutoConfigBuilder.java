package org.itsallcode.matcher.auto;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.emptyArray;

import java.io.File;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArray;
import org.hamcrest.collection.IsMapContaining;
import org.itsallcode.matcher.config.ConfigurableMatcher;
import org.itsallcode.matcher.config.MatcherConfig;
import org.itsallcode.matcher.config.MatcherConfig.Builder;

class AutoConfigBuilder<T> {

	private static final Logger LOG = Logger.getLogger(AutoConfigBuilder.class.getName());

	private static final Set<Class<?>> SIMPLE_TYPES = Collections.unmodifiableSet(new HashSet<>(asList(String.class,
			Long.class, Integer.class, Byte.class, Boolean.class, Float.class, Double.class, Character.class,
			Short.class, BigInteger.class, BigDecimal.class, Calendar.class, Date.class, java.sql.Date.class,
			java.sql.Timestamp.class, Instant.class, LocalDate.class,
			Temporal.class, Currency.class,
			File.class, Path.class, UUID.class, Class.class, Package.class, Enum.class, URL.class, URI.class)));

	private final T expected;
	private final Builder<T> configBuilder;

	private AutoConfigBuilder(final T expected) {
		this.expected = expected;
		this.configBuilder = MatcherConfig.builder(expected);
	}

	private MatcherConfig<T> build() {
		Arrays.stream(expected.getClass().getMethods()) //
				.filter(this::isNotBlackListed) //
				.filter(this::isGetterMethodName) //
				.filter(this::isGetterMethodSignature) //
				.sorted(Comparator.comparing(this::hasSimpleReturnType).reversed() //
						.thenComparing(this::hasArrayReturnType) //
						.thenComparing(Method::getName)) //
				.forEach(this::addConfigForGetter);
		return configBuilder.build();
	}

	public static <T> Matcher<T> createEqualToMatcher(final T expected) {
		final Class<? extends Object> type = expected.getClass();
		if (type.isArray()) {
			return createArrayMatcher(expected);
		}
		if (isSimpleType(type)) {
			return Matchers.equalTo(expected);
		}
		if (Map.class.isAssignableFrom(type)) {
			return createMapContainsMatcher(expected);
		}
		if (Iterable.class.isAssignableFrom(type)) {
			return createIterableContainsMatcher(expected);
		}
		if (Optional.class.isAssignableFrom(type)) {
			return createOptionalMatcher(expected);
		}
		final MatcherConfig<T> config = new AutoConfigBuilder<>(expected).build();
		return new ConfigurableMatcher<>(config);
	}

	@SuppressWarnings("unchecked")
	private static <T> Matcher<T> createArrayMatcher(final Object expected) {
		final Class<T> componentType = (Class<T>) expected.getClass().getComponentType();
		if (componentType.isPrimitive()) {
			return (Matcher<T>) Matchers.equalTo(expected);
		}
		final Object[] expectedArray = (Object[]) expected;
		if (expectedArray.length == 0) {
			return (Matcher<T>) emptyArray();
		}
		if (isSimpleType(componentType)) {
			final Matcher<Object[]> arrayContaining = Matchers.arrayContaining(expectedArray);
			return (Matcher<T>) arrayContaining;
		}
		final List<Matcher<?>> matchers = Arrays.stream(expectedArray).map(AutoMatcher::equalTo).collect(toList());
		@SuppressWarnings("rawtypes")
		final Matcher<Object[]> arrayContaining = IsArray.array(matchers.toArray(new Matcher[0]));
		return (Matcher<T>) arrayContaining;
	}

	@SuppressWarnings("unchecked")
	private static <T, K, V> Matcher<T> createMapContainsMatcher(final T expected) {
		final Map<K, V> expectedMap = (Map<K, V>) expected;

		final Collection<Matcher<? super T>> matchers = new ArrayList<>();

		matchers.add(mapSizeMatcher(expectedMap));

		for (final Entry<K, V> expectedEntry : expectedMap.entrySet()) {
			matchers.add((Matcher<? super T>) IsMapContaining.hasEntry(createEqualToMatcher(expectedEntry.getKey()),
					createEqualToMatcher(expectedEntry.getValue())));
		}
		return Matchers.allOf(matchers);
	}

	private static <T, K, V> ConfigurableMatcher<T> mapSizeMatcher(final Map<K, V> expectedMap) {
		@SuppressWarnings("unchecked")
		final MatcherConfig<T> config = (MatcherConfig<T>) MatcherConfig.builder(expectedMap)
				.addEqualsProperty("size", Map::size).build();
		return new ConfigurableMatcher<>(config);
	}

	private static <T> Matcher<T> createIterableContainsMatcher(final T expected) {
		@SuppressWarnings("unchecked")
		final Iterable<T> expectedIterable = (Iterable<T>) expected;
		final Object[] elements = StreamSupport.stream(expectedIterable //
				.spliterator(), false) //
				.toArray();
		@SuppressWarnings("unchecked")
		final Matcher<T> matcher = (Matcher<T>) AutoMatcher.contains(elements);
		return matcher;
	}

	@SuppressWarnings("unchecked")
	private static <T> Matcher<T> createOptionalMatcher(final T expected) {
		final Optional<T> expectedOptional = (Optional<T>) expected;
		if (expectedOptional.isEmpty()) {
			return (Matcher<T>) OptionalMatchers.isEmpty();
		}
		return (Matcher<T>) OptionalMatchers.isPresentAnd(AutoMatcher.equalTo(expectedOptional.get()));
	}

	private boolean isNotBlackListed(final Method method) {
		final Set<String> blacklist = new HashSet<>(
				asList("getClass", "getProtectionDomain", "getClassLoader", "getURLs"));
		return !blacklist.contains(method.getName());
	}

	private boolean isGetterMethodSignature(final Method method) {
		return method.getParameterCount() == 0 //
				&& !method.getReturnType().equals(Void.TYPE);
	}

	private boolean isGetterMethodName(final Method method) {
		final String methodName = method.getName();
		return methodName.startsWith("get") //
				|| methodName.startsWith("is");
	}

	private void addConfigForGetter(final Method method) {
		final String propertyName = getPropertyName(method.getName());
		LOG.finest(() -> "Adding general property '" + propertyName + "' for getter " + method);
		configBuilder.addProperty(propertyName, createGetter(method), AutoMatcher::equalTo);
	}

	private boolean hasArrayReturnType(final Method method) {
		return method.getReturnType().isArray();
	}

	private <P> Function<T, P> createGetter(final Method method) {
		return object -> getPropertyValue(method, object);
	}

	private boolean hasSimpleReturnType(final Method method) {
		final Class<? extends Object> type = method.getReturnType();
		if (type.isPrimitive() || type.isEnum()) {
			return true;
		}
		return isSimpleType(type);
	}

	private static boolean isSimpleType(final Class<? extends Object> type) {
		for (final Class<?> simpleType : SIMPLE_TYPES) {
			if (simpleType.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}

	static String getPropertyName(final String methodName) {
		final int prefixLength;
		if (methodName.startsWith("get")) {
			prefixLength = 3;
		} else if (methodName.startsWith("is")) {
			prefixLength = 2;
		} else {
			return methodName;
		}
		if (methodName.length() == prefixLength) {
			return methodName;
		}
		final String propertyName = methodName.substring(prefixLength);
		return decapitalize(propertyName);
	}

	private static String decapitalize(final String string) {
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	@SuppressWarnings({ "unchecked", "java:S3011" }) // Need to use reflection and setAccessible()
	private static <T, P> P getPropertyValue(final Method method, final T object) {
		final Class<?> declaringClass = method.getDeclaringClass();
		if (!declaringClass.isInstance(object)) {
			throw new AssertionError("Expected object of type " + declaringClass.getName() + " but got "
					+ object.getClass().getName() + ": " + object.toString());
		}
		if (!Modifier.isPublic(declaringClass.getModifiers())) {
			method.setAccessible(true);
		}
		try {
			return (P) method.invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException("Error invoking method " + method + " on object " + object + " of type "
					+ object.getClass().getName(), e);
		}
	}
}
