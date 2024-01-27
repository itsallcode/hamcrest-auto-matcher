package com.github.hamstercommunity.matcher.model;

import java.util.function.Function;

import org.hamcrest.Matcher;
import org.j8unit.runners.J8Unit4;
import org.junit.runner.RunWith;

import com.github.hamstercommunity.matcher.auto.AutoMatcher;

/**
 * Unit test for {@link AutoMatcher} using {@link DemoModel}
 */
@RunWith(J8Unit4.class)
public class AutoDemoModelMatcherTest implements DemoModelMatcherTest {
	@Override
	public Function<DemoModel, Matcher<DemoModel>> createNewSUT() {
		return AutoMatcher::equalTo;
	}
}
