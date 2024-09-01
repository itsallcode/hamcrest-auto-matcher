package org.itsallcode.matcher.model;

import java.util.function.Function;

import org.hamcrest.Matcher;

/**
 * Unit test for {@link DemoModelMatcher}
 */
class ConfigurableDemoModelMatcherTest extends DemoModelMatcherTest {
    @Override
    protected Function<DemoModel, Matcher<DemoModel>> createNewSUT() {
        return DemoModelMatcher::equalTo;
    }
}
