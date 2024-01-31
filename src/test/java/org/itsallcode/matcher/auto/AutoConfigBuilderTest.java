package org.itsallcode.matcher.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AutoConfigBuilderTest {

    @ParameterizedTest
    @CsvSource({ "getName,name", "getname,name", "isTrue,true", "istrue,true", "get,get", "is,is",
            "otherName,otherName", "OtherName,OtherName" })
    void getPropertyName(final String methodName, final String expectedPropertyName) {
        assertEquals(expectedPropertyName, AutoConfigBuilder.getPropertyName(methodName));
    }
}
