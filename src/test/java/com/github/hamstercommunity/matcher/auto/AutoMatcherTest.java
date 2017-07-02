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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.hamstercommunity.matcher.model.DemoAttribute;
import com.github.hamstercommunity.matcher.model.DemoModel;

public class AutoMatcherTest {

	private DemoModel value1;
	private DemoModel value2;
	private DemoModel value1Equal;
	private DemoModel value2Equal;

	@Before
	public void setup() {
		value1 = model("m", 1);
		value2 = model("m", 2);
		value1Equal = model("m", 1);
		value2Equal = model("m", 2);
	}

	@Test
	public void testEqualToEqualToSame() {
		assertThat(value1, AutoMatcher.equalTo(value1));
	}

	@Test
	public void testEqualToEqualToEqualValue() {
		assertThat(value1, AutoMatcher.equalTo(value1Equal));
	}

	@Test
	public void testEqualToNotEqual() {
		assertThat(value1, not(AutoMatcher.equalTo(value2)));
	}

	@Test(expected = NullPointerException.class)
	public void testEqualToNullThrowsNullPointerException() {
		AutoMatcher.equalTo(null);
	}

	@Test
	public void testContainsActualEmptyListSuccess() {
		assertThat(emptyList(), AutoMatcher.contains());
	}

	@Test
	public void testContainsActualEmptyListFailure() {
		assertThat(emptyList(), not(AutoMatcher.contains(value1)));
	}

	@Test
	public void testContainsActualNonEmptyListFailure() {
		assertThat(asList(value1), not(AutoMatcher.contains()));
	}

	@Test
	public void testContainsActualNonEmptyListSuccess() {
		assertThat(asList(value1), AutoMatcher.contains(value1));
	}

	@Test
	public void testContainsActualMultipleEqualEntriesSuccess() {
		assertThat(asList(value1, value2), AutoMatcher.contains(value1Equal, value2Equal));
	}

	@Test
	public void testContainsActualMultipleSameEntriesSuccess() {
		assertThat(asList(value1, value2), AutoMatcher.contains(value1, value2));
	}

	@Test
	public void testContainsActualMultipleEntriesWrongOrder() {
		assertThat(asList(value1, value2), not(AutoMatcher.contains(value2Equal, value1Equal)));
	}

	@Test
	public void testContainsActualMultipleEntriesExpectMoreEntries() {
		assertThat(asList(value1, value2), not(AutoMatcher.contains(value1Equal, value2Equal, value1Equal)));
	}

	@Test
	public void testContainsInAnyOrderActualEmptyListSuccess() {
		assertThat(emptyList(), AutoMatcher.containsInAnyOrder());
	}

	@Test
	public void testContainsInAnyOrderActualEmptyListFailure() {
		assertThat(emptyList(), not(AutoMatcher.containsInAnyOrder(value1)));
	}

	@Test
	public void testContainsInAnyOrderActualNonEmptyListFailure() {
		assertThat(asList(value1), not(AutoMatcher.containsInAnyOrder()));
	}

	@Test
	public void testContainsInAnyOrderActualNonEmptyListSuccess() {
		assertThat(asList(value1), AutoMatcher.containsInAnyOrder(value1));
	}

	@Test
	public void testContainsInAnyOrderActualMultipleEqualEntriesSuccess() {
		assertThat(asList(value1, value2), AutoMatcher.containsInAnyOrder(value1Equal, value2Equal));
	}

	@Test
	public void testContainsInAnyOrderActualMultipleSameEntriesSuccess() {
		assertThat(asList(value1, value2), AutoMatcher.containsInAnyOrder(value1, value2));
	}

	@Test
	public void testContainsInAnyOrderActualMultipleEntriesWrongOrder() {
		assertThat(asList(value1, value2), AutoMatcher.containsInAnyOrder(value2Equal, value1Equal));
	}

	@Test
	public void testContainsInAnyOrderActualMultipleEntriesExpectMoreEntries() {
		assertThat(asList(value1, value2), not(AutoMatcher.containsInAnyOrder(value1Equal, value2Equal, value1Equal)));
	}

	private DemoModel model(String name, int id) {
		return model(name, id,
				asList(model(name + "-child1", id, emptyList()), model(name + "-child2", id, emptyList())));
	}

	private DemoModel model(String name, int id, List<DemoModel> children) {
		return new DemoModel(id, name + "-" + id, id * 2L, attr(name + "-attr-" + id),
				new String[] { name + "-item1-" + id, name + "-item2-" + id }, children);
	}

	private DemoAttribute attr(String value) {
		return new DemoAttribute(value);
	}
}
