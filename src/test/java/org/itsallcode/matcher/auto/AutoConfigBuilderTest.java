package org.itsallcode.matcher.auto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AutoConfigBuilderTest {

    @ParameterizedTest
    @CsvSource({ "getName,name", "getname,name", "isTrue,true", "istrue,true", "get,get", "is,is",
            "otherName,otherName", "OtherName,OtherName" })
    void getPropertyName(final String methodName, final String expectedPropertyName) {
        assertEquals(expectedPropertyName, AutoConfigBuilder.getPropertyName(methodName));
    }

    @Test
    void buildFailsWhenNoProperties() {
        final AutoConfigBuilder<ClassWithNoProperties> builder = AutoConfigBuilder.create(new ClassWithNoProperties());
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, builder::build);
        assertThat(exception.getMessage(), Matchers.equalTo(
                "Failed to build MatcherConfig: Class org.itsallcode.matcher.auto.AutoConfigBuilderTest$ClassWithNoProperties has no properties."));
    }

    static class ClassWithNoProperties {
        // Empty by intention
    }
}
