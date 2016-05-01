package com.github.hamstercommunity.matcher.model;

import com.github.hamstercommunity.matcher.ConfigurableMatcher;
import com.github.hamstercommunity.matcher.MatcherConfig;

public class DemoModelMatcher extends ConfigurableMatcher<DemoModel> {

	private DemoModelMatcher(DemoModel expected) {
		super(MatcherConfig.builder(expected) //
				.addEqualsProperty("id", DemoModel::getId) //
				.addEqualsProperty("name", DemoModel::getName) //
				.addProperty("attr", DemoModel::getAttr, DemoAttributeMatcher::equalTo) //
				.addIterableProperty("children", DemoModel::getChildren, DemoModelMatcher::equalTo) //
				.build());
	}

	public static DemoModelMatcher equalTo(DemoModel expected) {
		return new DemoModelMatcher(expected);
	}
}
