package org.itsallcode.matcher.model;

import org.itsallcode.matcher.config.ConfigurableMatcher;

/**
 * A model class used for testing and demonstrating {@link ConfigurableMatcher}.
 */
public class DemoAttribute {
    private final String value;

    public DemoAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DemoAttribute [value=" + value + "]";
    }
}