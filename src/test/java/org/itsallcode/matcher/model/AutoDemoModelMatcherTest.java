package org.itsallcode.matcher.model;

import java.util.function.Function;

import org.hamcrest.Matcher;
import org.itsallcode.matcher.auto.AutoMatcher;

/**
 * Unit test for {@link AutoMatcher} using {@link DemoModel}
 */
class AutoDemoModelMatcherTest extends DemoModelMatcherTest {
    @Override
    protected Function<DemoModel, Matcher<DemoModel>> createNewSUT() {
        return AutoMatcher::equalTo;
    }
}
