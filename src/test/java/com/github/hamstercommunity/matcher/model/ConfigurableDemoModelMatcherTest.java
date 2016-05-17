/**
 * Automatic hamcrest matcher for model classes
 * Copyright (C) 2016 Christoph Pirkl <christoph at users.sourceforge.net>
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
