package org.itsallcode.matcher.model;

import java.util.function.Function;

import org.hamcrest.Matcher;
import org.j8unit.runners.J8Unit4;
import org.junit.runner.RunWith;

/**
 * Unit test for {@link DemoModelMatcher}
 */
@RunWith(J8Unit4.class)
public class ConfigurableDemoModelMatcherTest implements DemoModelMatcherTest {
	@Override
	public Function<DemoModel, Matcher<DemoModel>> createNewSUT() {
		return DemoModelMatcher::equalTo;
	}
}
