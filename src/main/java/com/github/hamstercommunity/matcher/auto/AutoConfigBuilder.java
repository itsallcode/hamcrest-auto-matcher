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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

import com.github.hamstercommunity.matcher.config.MatcherConfig;
import com.github.hamstercommunity.matcher.config.MatcherConfig.Builder;

class AutoConfigBuilder<T> {

	private final T expected;
	private final Builder<T> configBuilder;

	AutoConfigBuilder(T expected) {
		this.expected = expected;
		this.configBuilder = MatcherConfig.builder(expected);
	}

	MatcherConfig<T> build() {
		Arrays.stream(expected.getClass().getMethods()) //
				.filter(this::isGetterMethodName) //
				.filter(this::isGetterMethodSignature) //
				.forEach(this::addConfigForGetter);
		return configBuilder.build();
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

		if (isSimpleType(propertyType)) {
			configBuilder.addEqualsProperty(propertyName, createGetter(method));
		} else if (isIterableType(propertyType)) {
			// configBuilder.addIterableProperty(propertyName,
			// createGetter(method), )
		} else {
			configBuilder.addProperty(propertyName, createGetter(method), AutoMatcher::equalTo);
		}
	}

	private boolean isIterableType(Class<?> propertyType) {
		return propertyType.isInstance(Iterable.class);
	}

	private <P> Function<T, P> createGetter(Method method) {
		return (object) -> getPropertyValue(method, object);
	}

	private boolean isSimpleType(Class<? extends Object> type) {
		return type.isPrimitive() //
				|| type.isEnum() //
				|| type.isInstance(String.class);
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
