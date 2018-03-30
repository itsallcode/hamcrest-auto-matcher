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

import static com.github.hamstercommunity.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static com.github.hamstercommunity.matcher.auto.TestUtil.assertValuesMatch;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Test;

public class AutoMatcherMapTest {

	@Test
	public void testEmptyMap() {
		assertValuesDoNotMatch(emptyMap(), singletonMap("key2", "value2"));
	}

	@Test
	public void testEmptyMapAndNewHashMap() {
		assertValuesMatch(new HashMap<>(), emptyMap());
	}

	@Test
	public void testEmptyMapAndNewTreeMap() {
		assertValuesMatch(new TreeMap<>(), emptyMap());
	}

	@Test
	public void testSingletonMap() {
		assertValuesDoNotMatch(singletonMap("key1", "value1"), singletonMap("key2", "value2"));
	}

	@Test
	public void testSingletonMapAndEmptyMap() {
		assertValuesDoNotMatch(singletonMap("key1", "value1"), emptyMap());
	}

	@Test
	public void testSingletonMapAndNewHashMap() {
		assertValuesDoNotMatch(singletonMap("key1", "value1"), new HashMap<>());
	}

	@Test(expected = ClassCastException.class)
	public void testIncompatibleTypesClassNotPublic() {
		assertThat(singletonList("value1"), AutoMatcher.equalTo(singletonMap("key", "value")));
	}

	@Test
	public void testAutoMatcherWorksForSingletonMapAndNewHashMapWith1Entry() {
		final HashMap<Object, Object> map = new HashMap<>();
		map.put("key1", "value1");
		assertValuesMatch(singletonMap("key1", "value1"), map);
	}

	@Test
	public void testSingletonMapAndNewHashMapWith1Entry() {
		final HashMap<Object, Object> map = new HashMap<>();
		map.put("key1", "value1");
		assertValuesMatch(singletonMap("key1", "value1"), map);
	}
}
