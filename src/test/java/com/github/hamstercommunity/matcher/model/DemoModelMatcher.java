/**
 * Automatic hamcrest matcher for model classes
 * Copyright (C) 2017 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.hamstercommunity.matcher.model;

import org.hamcrest.Matcher;

import com.github.hamstercommunity.matcher.config.ConfigurableMatcher;
import com.github.hamstercommunity.matcher.config.MatcherConfig;

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
