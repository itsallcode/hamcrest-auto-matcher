/**
 * Automatic hamcrest matcher for model classes
 * Copyright (C) 2016 Christoph Pirkl <christoph at users.sourceforge.net>
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;

import com.github.hamstercommunity.matcher.config.MatcherConfig;
import com.github.hamstercommunity.matcher.config.MatcherConfig.Builder;

class AutoConfigBuilder<T> {

	private final static Logger LOG = Logger.getLogger(AutoConfigBuilder.class.getName());

	private final static Set<Class<?>> SIMPLE_TYPES = Collections
			.unmodifiableSet(new HashSet<>(asList(String.class, Long.class)));

	private final T expected;
	private final Builder<T> configBuilder;

	AutoConfigBuilder(T expected) {
		this.expected = expected;
		this.configBuilder = MatcherConfig.builder(expected);
	}

	MatcherConfig<T> build() {
		Arrays.stream(expected.getClass().getMethods()) //
				.filter(this::isNotBlackListed) //
				.filter(this::isGetterMethodName) //
				.filter(this::isGetterMethodSignature) //
				.sorted(Comparator.comparing(this::hasSimpleReturnType).reversed() //
						.thenComparing(this::hasIterableReturnType) //
						.thenComparing(this::hasArrayReturnType) //
						.thenComparing(Method::getName)) //
				.forEach(this::addConfigForGetter);
		return configBuilder.build();
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
		final Class<?> propertyType = method.getReturnType();
		final String propertyName = getPropertyName(method.getName());

		if (hasSimpleReturnType(method)) {
			LOG.finest(() -> "Adding property '" + propertyName + "' with simple type " + propertyType.getName());
			configBuilder.addEqualsProperty(propertyName, createGetter(method));
		} else if (hasIterableReturnType(method)) {
			LOG.finest(() -> "Adding iterable property '" + propertyName + "'");
			configBuilder.addIterableProperty(propertyName, createGetter(method), AutoMatcher::equalTo);
		} else {
			LOG.finest(() -> "Adding general property '" + propertyName + "' with type " + propertyType);
			configBuilder.addProperty(propertyName, createGetter(method), AutoMatcher::equalTo);
		}
	}

	private boolean hasArrayReturnType(Method method) {
		return method.getReturnType().isArray();
	}

	private boolean hasIterableReturnType(Method method) {
		final Class<?> propertyType = method.getReturnType();
		return Iterable.class.isAssignableFrom(propertyType) //
				&& propertyType.getTypeParameters().length == 1;
	}

	private <P> Function<T, P> createGetter(Method method) {
		return (object) -> getPropertyValue(method, object);
	}

	private boolean hasSimpleReturnType(Method method) {
		final Class<? extends Object> type = method.getReturnType();
		if (type.isPrimitive() || type.isEnum()) {
			return true;
		}
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
		try {
			return (P) method.invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Error invoking method " + method + " on object " + object, e);
		}
	}
}
