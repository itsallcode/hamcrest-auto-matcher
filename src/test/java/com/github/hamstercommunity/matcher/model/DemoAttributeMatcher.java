package com.github.hamstercommunity.matcher.model;

import com.github.hamstercommunity.matcher.config.ConfigurableMatcher;
import com.github.hamstercommunity.matcher.config.MatcherConfig;

/**
 * A matcher for {@link DemoAttribute} used for testing
 * {@link ConfigurableMatcher} and demonstrating its usage.
 */
public class DemoAttributeMatcher extends ConfigurableMatcher<DemoAttribute> {

	private DemoAttributeMatcher(DemoAttribute expected) {
		super(MatcherConfig.builder(expected) //
				.addEqualsProperty("value", DemoAttribute::getValue) //
				.build());
	}

	public static DemoAttributeMatcher equalTo(DemoAttribute expected) {
		return new DemoAttributeMatcher(expected);
	}
}
