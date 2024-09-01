package org.itsallcode.matcher.config;

import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import org.hamcrest.*;

/**
 * Configuration for a {@link ConfigurableMatcher}.
 * 
 * @param <T> type of the object that is handled by the matcher
 */
public final class MatcherConfig<T> {
    private final T expected;
    private final List<PropertyConfig<T, ?>> propertyConfigs;

    private MatcherConfig(final T expected, final List<PropertyConfig<T, ?>> propertyConfigs) {
        this.expected = expected;
        this.propertyConfigs = Collections.unmodifiableList(propertyConfigs);
    }

    T getExpected() {
        return this.expected;
    }

    /**
     * Get the property configurations.
     * 
     * @return property configurations
     */
    @SuppressWarnings("unchecked")
    public List<PropertyConfig<T, Object>> getPropertyConfigs() {
        return this.propertyConfigs.stream() //
                .map(c -> (PropertyConfig<T, Object>) c) //
                .collect(toList());
    }

    /**
     * Create a new {@link Builder} for the given expected model.
     * 
     * @param <B>      The type of the model to compare.
     * @param expected the expected model
     * @return new builder
     */
    public static <B> Builder<B> builder(final B expected) {
        return new Builder<>(expected);
    }

    /**
     * Builder for {@link MatcherConfig}.
     * 
     * @param <B> The type of the model to compare.
     */
    public static final class Builder<B> {
        private final B expected;
        private final List<PropertyConfig<B, ?>> properties = new ArrayList<>();

        private Builder(final B expected) {
            this.expected = Objects.requireNonNull(expected);
        }

        /**
         * Add a property that can be compared with {@link Matchers#equalTo(Object)}.
         *
         * @param propertyName     name of the property.
         * @param propertyAccessor the accessor function for retrieving the property
         *                         value.
         * @param <P>              the type of the property.
         * @return the builder itself for fluent programming style.
         */
        public <P> Builder<B> addEqualsProperty(final String propertyName, final Function<B, P> propertyAccessor) {
            return addProperty(propertyName, propertyAccessor, Matchers::equalTo);
        }

        /**
         * Add a property that can be compared with {@link Matchers#equalTo(Object)}.
         *
         * @param propertyName     name of the property.
         * @param propertyAccessor the accessor function for retrieving the property
         *                         value.
         * @param matcherBuilder   a function for creating the matcher.
         * @param <P>              the type of the property.
         * @return the builder itself for fluent programming style.
         */
        public <P> Builder<B> addProperty(final String propertyName, final Function<B, P> propertyAccessor,
                final Function<P, Matcher<P>> matcherBuilder) {
            final Matcher<P> matcher = createMatcher(propertyAccessor, matcherBuilder);
            return addPropertyInternal(propertyName, matcher, propertyAccessor);
        }

        @SuppressWarnings("unchecked")
        private <P> Matcher<P> createMatcher(final Function<B, P> propertyAccessor,
                final Function<P, Matcher<P>> matcherBuilder) {
            final P expectedValue = propertyAccessor.apply(this.expected);
            if (expectedValue == null) {
                return (Matcher<P>) Matchers.nullValue();
            }
            return matcherBuilder.apply(expectedValue);
        }

        /**
         * Add a property of type {@link Iterable} where the element order is relevant.
         *
         * @param propertyName     name of the property.
         * @param propertyAccessor the accessor function for retrieving the property
         *                         value.
         * @param matcherBuilder   a function for creating the matcher for the iterable
         *                         elements.
         * @param <P>              the type of the property.
         * @return the builder itself for fluent programming style.
         */
        public <P> Builder<B> addIterableProperty(final String propertyName,
                final Function<B, Iterable<? extends P>> propertyAccessor,
                final Function<P, Matcher<P>> matcherBuilder) {
            final Iterable<? extends P> expectedPropertyValue = propertyAccessor.apply(this.expected);
            final Matcher<Iterable<? extends P>> listMatcher = createListMatcher(matcherBuilder, expectedPropertyValue);
            return addPropertyInternal(propertyName, listMatcher, propertyAccessor);
        }

        private <P> Matcher<Iterable<? extends P>> createListMatcher(final Function<P, Matcher<P>> matcherBuilder,
                final Iterable<? extends P> expectedPropertyValue) {
            if (expectedPropertyValue == null) {
                return createNullIterableMatcher();
            }
            if (!expectedPropertyValue.iterator().hasNext()) {
                return Matchers.<P>emptyIterable();
            }
            final List<Matcher<? super P>> matchers = StreamSupport.stream(expectedPropertyValue.spliterator(), false)
                    .map(matcherBuilder) //
                    .collect(toList());
            return Matchers.contains(matchers);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private <P> Matcher<Iterable<? extends P>> createNullIterableMatcher() {
            return new NullIterableMatcher();
        }

        private <P> Builder<B> addPropertyInternal(final String propertyName, final Matcher<P> matcher,
                final Function<B, P> propertyAccessor) {
            this.properties.add(new PropertyConfig<>(propertyName, matcher, propertyAccessor));
            return this;
        }

        /**
         * Build a new {@link MatcherConfig}.
         *
         * @return the new {@link MatcherConfig}.
         */
        public MatcherConfig<B> build() {
            if (this.properties.isEmpty()) {
                throw new IllegalArgumentException("Failed to build MatcherConfig: Class "
                        + this.expected.getClass().getName() + " has no properties.");
            }
            return new MatcherConfig<>(this.expected, new ArrayList<>(this.properties));
        }
    }

    private static class NullIterableMatcher<T> extends BaseMatcher<Iterable<T>> {
        @Override
        public boolean matches(final Object item) {
            return item == null;
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("null");
        }
    }
}
