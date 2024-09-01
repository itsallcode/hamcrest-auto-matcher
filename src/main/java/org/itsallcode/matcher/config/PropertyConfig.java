package org.itsallcode.matcher.config;

import java.util.function.Function;

import org.hamcrest.Matcher;

/**
 * This is used by {@link MatcherConfig} to store configuration for a single
 * property.
 * 
 * @param <T> type of the object that is handled by the matcher
 * @param <P> type of the object's property
 */
public class PropertyConfig<T, P> {
    private final String propertyName;
    private final Matcher<P> matcher;
    private final Function<T, P> propertyAccessor;

    PropertyConfig(final String propertyName, final Matcher<P> matcher, final Function<T, P> propertyAccessor) {
        this.propertyName = propertyName;
        this.matcher = matcher;
        this.propertyAccessor = propertyAccessor;
    }

    /**
     * Get the name of the property.
     * 
     * @return property name
     */
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Get the matcher that is used to compare the property value.
     * 
     * @return matcher
     */
    public Matcher<P> getMatcher() {
        return this.matcher;
    }

    /**
     * Get the property value from the object.
     * 
     * @param object object to get the property value from
     * @return property value
     */
    public P getPropertyValue(final T object) {
        return this.propertyAccessor.apply(object);
    }
}
