package com.github.hamstercommunity.matcher.config;

import java.util.function.Function;

import org.hamcrest.Matcher;

/**
 * This is used by {@link MatcherConfig} to store configuration for a single
 * property.
 * 
 * @param <T> type of the object that is handled by the matcher
 * @param <P> type of the object's property
 */
class PropertyConfig<T, P> {
	private final String propertyName;
	private final Matcher<P> matcher;
	private final Function<T, P> propertyAccessor;

	PropertyConfig(final String propertyName, final Matcher<P> matcher, final Function<T, P> propertyAccessor) {
		this.propertyName = propertyName;
		this.matcher = matcher;
		this.propertyAccessor = propertyAccessor;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public Matcher<P> getMatcher() {
		return this.matcher;
	}

	public P getPropertyValue(final T object) {
		return this.propertyAccessor.apply(object);
	}
}