package org.itsallcode.matcher.auto;

import java.util.Optional;

import org.hamcrest.*;

/**
 * Provides a set of Hamcrest matchers for {@code java.util.Optional}:
 * <ul>
 * <li>{@link #isEmpty()} - matches when the examined {@code Optional} contains
 * no value.</li>
 * <li>{@link #isPresentAnd(Matcher)} - matches when the examined
 * {@code Optional} contains a value that satisfies the specified matcher.</li>
 * </ul>
 *
 * This was copied from
 * {@link https://github.com/npathai/hamcrest-optional/blob/master/src/main/java/com/github/npathai/hamcrestopt/OptionalMatchers.java}
 * 
 * @author npathai, sweiler
 */
class OptionalMatchers {

    /**
     * Creates a matcher that matches when the examined {@code Optional} contains no
     * value.
     * 
     * <pre>
     * Optional&lt;String&gt; optionalObject = Optional.empty();
     * assertThat(optionalObject, isEmpty());
     * </pre>
     *
     * @return a matcher that matches when the examined {@code Optional} contains no
     *         value.
     */
    static Matcher<Optional<?>> isEmpty() {
        return new EmptyMatcher();
    }

    private static class EmptyMatcher extends TypeSafeMatcher<Optional<?>> {

        public void describeTo(final Description description) {
            description.appendText("is <Empty>");
        }

        @Override
        protected boolean matchesSafely(final Optional<?> item) {
            return !item.isPresent();
        }

        @Override
        protected void describeMismatchSafely(final Optional<?> item, final Description mismatchDescription) {
            mismatchDescription.appendText("had value ");
            mismatchDescription.appendValue(item.get());
        }
    }

    /**
     * Creates a matcher that matches when the examined {@code Optional} contains a
     * value that satisfies the specified matcher.
     * 
     * <pre>
     * Optional&lt;String&gt; optionalObject = Optional.of("dummy value");
     * assertThat(optionalObject, isPresentAnd(startsWith("dummy")));
     * </pre>
     *
     * @param matcher a matcher for the value of the examined {@code Optional}.
     * @param <T>     the class of the value.
     * @return a matcher that matches when the examined {@code Optional} contains a
     *         value that satisfies the specified matcher.
     */
    public static <T> Matcher<Optional<T>> isPresentAnd(final Matcher<? super T> matcher) {
        return new HasValue<>(matcher);
    }

    private static class HasValue<T> extends TypeSafeMatcher<Optional<T>> {
        private final Matcher<? super T> matcher;

        public HasValue(final Matcher<? super T> matcher) {
            this.matcher = matcher;
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("has value that is ");
            matcher.describeTo(description);
        }

        @Override
        protected boolean matchesSafely(final Optional<T> item) {
            return item.isPresent() && matcher.matches(item.get());
        }

        @Override
        protected void describeMismatchSafely(final Optional<T> item, final Description mismatchDescription) {
            if (item.isPresent()) {
                mismatchDescription.appendText("value ");
                matcher.describeMismatch(item.get(), mismatchDescription);
            } else {
                mismatchDescription.appendText("was <Empty>");
            }
        }
    }

    // This is an utility class that must not be instantiated.
    private OptionalMatchers() {
    }
}
