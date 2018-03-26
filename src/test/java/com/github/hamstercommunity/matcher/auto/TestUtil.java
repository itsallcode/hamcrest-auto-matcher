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
package com.github.hamstercommunity.matcher.auto;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class TestUtil {
	private TestUtil() {
		// not instantiable
	}

	public static <T> void assertValuesMatch(T value1, T value2) {
		assertThat(value1, AutoMatcher.equalTo(value1));
		assertThat(value2, AutoMatcher.equalTo(value2));
		assertThat(value2, AutoMatcher.equalTo(value1));
		assertThat(value1, AutoMatcher.equalTo(value2));
	}

	public static <T> void assertValuesDoNotMatch(T value1, T value2) {
		assertThat(value1, not(AutoMatcher.equalTo(value2)));
		assertThat(value1, AutoMatcher.equalTo(value1));
		assertThat(value2, AutoMatcher.equalTo(value2));
	}
}
