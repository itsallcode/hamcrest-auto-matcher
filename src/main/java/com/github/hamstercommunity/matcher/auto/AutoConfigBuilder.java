/**
 * Automatic hamcrest matcher for model classes
 * Copyright (C) 2017 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.hamstercommunity.matcher.auto;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.emptyArray;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArray;
import org.hamcrest.collection.IsMapContaining;

import com.github.hamstercommunity.matcher.config.ConfigurableMatcher;
import com.github.hamstercommunity.matcher.config.MatcherConfig;
import com.github.hamstercommunity.matcher.config.MatcherConfig.Builder;

class AutoConfigBuilder<T> {

	private static final Logger LOG = Logger.getLogger(AutoConfigBuilder.class.getName());

	private static final Set<Class<?>> SIMPLE_TYPES = Collections.unmodifiableSet(new HashSet<>(asList(String.class,
			Long.class, Integer.class, Byte.class, Boolean.class, Float.class, Double.class, Character.class,
			Short.class, BigInteger.class, BigDecimal.class, Calendar.class, Date.class, Temporal.class, Currency.class,
			File.class, Path.class, UUID.class, Class.class, Package.class, Enum.class)));

	private final T expected;
	private final Builder<T> configBuilder;

	private AutoConfigBuilder(T expected) {
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

	public static <T> Matcher<T> createEqualToMatcher(T expected) {
		if (expected.getClass().isArray()) {
			return createArrayMatcher(expected);
		}
		if (isSimpleType(expected.getClass())) {
			return Matchers.equalTo(expected);
		}
		if (Map.class.isAssignableFrom(expected.getClass())) {
			return createMapContainsMatcher(expected);
		}
		if (Iterable.class.isAssignableFrom(expected.getClass())) {
			return createIterableContainsMatcher(expected);
		}
		final MatcherConfig<T> config = new AutoConfigBuilder<>(expected).build();
		return new ConfigurableMatcher<>(config);
	}

	@SuppressWarnings("unchecked")
	private static <T> Matcher<T> createArrayMatcher(Object expected) {
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
		final Matcher<Object[]> arrayContaining = IsArray.array(matchers.toArray(new Matcher[0]));
		return (Matcher<T>) arrayContaining;
	}

	@SuppressWarnings("unchecked")
	private static <T, K, V> Matcher<T> createMapContainsMatcher(T expected) {
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
				.addEqualsProperty("size", map -> map.size()).build();
		return new ConfigurableMatcher<>(config);
	}

	private static <T> Matcher<T> createIterableContainsMatcher(T expected) {
		@SuppressWarnings("unchecked")
		final Iterable<T> expectedIterable = (Iterable<T>) expected;
		final Object[] elements = StreamSupport.stream(expectedIterable //
				.spliterator(), false) //
				.toArray();
		@SuppressWarnings("unchecked")
		final Matcher<T> matcher = (Matcher<T>) AutoMatcher.contains(elements);
		return matcher;
	}

	private boolean isNotBlackListed(Method method) {
		final Set<String> blacklist = new HashSet<>(
				asList("getClass", "getProtectionDomain", "getClassLoader", "getURLs"));
		return !blacklist.contains(method.getName());
	}

	private boolean isGetterMethodSignature(Method method) {
		return method.getParameterCount() == 0 //
				&& !method.getReturnType().equals(Void.TYPE);
	}

	private boolean isGetterMethodName(Method method) {
		final String methodName = method.getName();
		return methodName.startsWith("get") //
				|| methodName.startsWith("is");
	}

	private void addConfigForGetter(Method method) {
		final String propertyName = getPropertyName(method.getName());
		LOG.finest(() -> "Adding general property '" + propertyName + "' for getter " + method);
		configBuilder.addProperty(propertyName, createGetter(method), AutoMatcher::equalTo);
	}

	private boolean hasArrayReturnType(Method method) {
		return method.getReturnType().isArray();
	}

	private <P> Function<T, P> createGetter(Method method) {
		return (object) -> getPropertyValue(method, object);
	}

	private boolean hasSimpleReturnType(Method method) {
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

	private String getPropertyName(String methodName) {
		int prefixLength = 3;
		if (methodName.startsWith("is")) {
			prefixLength = 2;
		}
		final String propertyName = methodName.substring(prefixLength);
		return decapitalize(propertyName);
	}

	private String decapitalize(final String string) {
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	@SuppressWarnings("unchecked")
	private static <T, P> P getPropertyValue(Method method, T object) {
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
			throw new RuntimeException("Error invoking method " + method + " on object " + object + " of type "
					+ object.getClass().getName(), e);
		}
	}
}
