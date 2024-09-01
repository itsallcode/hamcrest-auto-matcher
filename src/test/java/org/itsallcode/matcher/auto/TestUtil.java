package org.itsallcode.matcher.auto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.Matcher;

public class TestUtil {
    private TestUtil() {
        // not instantiable
    }

    public static <T> void assertValuesMatch(final T value1, final T value2) {
        assertThat(value1, AutoMatcher.equalTo(value1));
        assertThat(value2, AutoMatcher.equalTo(value2));
        assertThat(value2, AutoMatcher.equalTo(value1));
        assertThat(value1, AutoMatcher.equalTo(value2));
    }

    public static <T> void assertValuesDoNotMatch(final T value1, final T value2) {
        assertThat("expect not match", value1, not(AutoMatcher.equalTo(value2)));
        assertThat("expect match", value1, AutoMatcher.equalTo(value1));
        assertThat("expect match", value2, AutoMatcher.equalTo(value2));
    }

    public static <T> void assertValuesDoNotMatch(final T value1, final T value2,
            final Matcher<String> expectedMessage) {
        assertValuesDoNotMatch(value1, value2);
        final Matcher<T> matcher = AutoMatcher.equalTo(value2);
        final AssertionError error = assertThrows(AssertionError.class,
                () -> assertThat(value1, matcher));
        assertThat(error.getMessage(), expectedMessage);
    }
}
