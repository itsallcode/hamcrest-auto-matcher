package com.github.hamstercommunity.matcher.auto;

import static com.github.hamstercommunity.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static com.github.hamstercommunity.matcher.auto.TestUtil.assertValuesMatch;
import static java.util.Collections.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
