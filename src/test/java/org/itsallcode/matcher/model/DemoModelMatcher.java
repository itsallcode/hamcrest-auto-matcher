package org.itsallcode.matcher.model;

import org.hamcrest.Matcher;
import org.itsallcode.matcher.config.ConfigurableMatcher;
import org.itsallcode.matcher.config.MatcherConfig;

/**
 * A matcher for {@link DemoModel} used for testing {@link ConfigurableMatcher}
 * and demonstrating its usage.
 */
public class DemoModelMatcher {

	public static Matcher<DemoModel> equalTo(DemoModel expected) {
		final MatcherConfig<DemoModel> config = MatcherConfig.builder(expected) //
				.addEqualsProperty("id", DemoModel::getId) //
				.addEqualsProperty("longVal", DemoModel::getLongVal) //
				.addEqualsProperty("name", DemoModel::getName) //
				.addProperty("attr", DemoModel::getAttr, DemoAttributeMatcher::equalTo) //
				.addIterableProperty("children", DemoModel::getChildren, DemoModelMatcher::equalTo) //
				.addEqualsProperty("stringArray", DemoModel::getStringArray).build();
		return new ConfigurableMatcher<>(config);
	}
}
