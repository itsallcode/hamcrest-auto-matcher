package org.itsallcode.matcher.auto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.hamcrest.Matcher;
import org.itsallcode.matcher.config.MatcherConfig;
import org.itsallcode.matcher.config.PropertyConfig;
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
        final AssertionError error = assertThrows(AssertionError.class, () -> assertThat(actual, matcher));
        assertThat(error.getMessage(), containsString("{name was \"Alice\"}"));
    }

    @Test
    void config() {
        final MatcherConfig<Record> config = AutoConfigBuilder.create(new Record("Alice", 42)).build();
        final List<PropertyConfig<Record, Object>> properties = config.getPropertyConfigs();
        assertThat(properties, hasSize(2));
        assertThat(properties.get(0).getPropertyName(), equalTo("age"));
        assertThat(properties.get(1).getPropertyName(), equalTo("name"));
    }

    static record Record(String name, int age) {
    }
}
