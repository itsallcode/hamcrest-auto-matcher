package org.itsallcode.matcher.auto;

import static java.util.Collections.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesMatch;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class AutoMatcherMapTest {

	@Test
	void testEmptyMap() {
		assertValuesDoNotMatch(emptyMap(), singletonMap("key2", "value2"));
	}

	@Test
	void testEmptyMapAndNewHashMap() {
		assertValuesMatch(new HashMap<>(), emptyMap());
	}

	@Test
	void testEmptyMapAndNewTreeMap() {
		assertValuesMatch(new TreeMap<>(), emptyMap());
	}

	@Test
	void testSingletonMap() {
		assertValuesDoNotMatch(singletonMap("key1", "value1"), singletonMap("key2", "value2"));
	}

	@Test
	void testSingletonMapAndEmptyMap() {
		assertValuesDoNotMatch(singletonMap("key1", "value1"), emptyMap());
	}

	@Test
	void testSingletonMapAndNewHashMap() {
		assertValuesDoNotMatch(singletonMap("key1", "value1"), new HashMap<>());
	}

	@Test
	void testIncompatibleTypesClassNotPublic() {
		assertThrows(ClassCastException.class,
				() -> assertThat(singletonList("value1"), AutoMatcher.equalTo(singletonMap("key", "value"))));
	}

	@Test
	void testAutoMatcherWorksForSingletonMapAndNewHashMapWith1Entry() {
		final HashMap<Object, Object> map = new HashMap<>();
		map.put("key1", "value1");
		assertValuesMatch(singletonMap("key1", "value1"), map);
	}

	@Test
	void testSingletonMapAndNewHashMapWith1Entry() {
		final HashMap<Object, Object> map = new HashMap<>();
		map.put("key1", "value1");
		assertValuesMatch(singletonMap("key1", "value1"), map);
	}
}
