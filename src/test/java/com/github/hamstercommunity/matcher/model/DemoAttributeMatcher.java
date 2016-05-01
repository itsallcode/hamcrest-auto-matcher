package com.github.hamstercommunity.matcher.model;

import com.github.hamstercommunity.matcher.ConfigurableMatcher;
import com.github.hamstercommunity.matcher.MatcherConfig;

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
