package org.itsallcode.matcher.auto;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

class AutoMatcherRecordTest {

    @Test
    void recordsEqual() {
        assertThat(new Record("Alice", 42), AutoMatcher.equalTo(new Record("Alice", 42)));
    }

    @Test
    void recordsNotEqual() {
        final Record actual = new Record("Alice", 42);
        final Matcher<Record> matcher = AutoMatcher.equalTo(new Record("Bob", 42));
        assertThat(actual, matcher);
    }

    static record Record(String name, int age) {
    }
}
