package org.itsallcode.matcher.auto;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesMatch;

import java.util.ArrayList;
import java.util.LinkedList;

import org.itsallcode.matcher.model.DemoAttribute;
import org.junit.jupiter.api.Test;

class AutoMatcherListTest {

	@Test
	void testEmptyList() {
		assertValuesDoNotMatch(emptyList(), singletonList("value2"));
	}

	@Test
	void testIncompatibleMemberTypes() {
		assertValuesDoNotMatch(asList("string"), asList(1));
	}

	@Test
	void testIncompatibleMemberTypesComplexTypes() {
		assertValuesDoNotMatch(asList(new DemoAttribute("attr")), asList(1));
	}

	@Test
	void testEmptyListAndNewArrayList() {
		assertValuesMatch(emptyList(), new ArrayList<>());
	}

	@Test
	void testEmptyListAndNewLinkedList() {
		assertValuesMatch(emptyList(), new LinkedList<>());
	}

	@Test
	void testEmptyListAndAsListAreEqaul() {
		assertValuesMatch(emptyList(), asList());
	}

	@Test
	void testSingletonList() {
		assertValuesDoNotMatch(singletonList("value1"), singletonList("value2"));
	}

	@Test
	void testAsListWithDuplicateValue() {
		assertValuesDoNotMatch(asList("value1"), asList("value1", "value1"));
	}

	@Test
	void testSingletonListAndNewArrayListWith1Entry() {
		final ArrayList<String> list = new ArrayList<>();
		list.add("value1");
		assertValuesMatch(singletonList("value1"), list);
	}

	@Test
	void testAsListAndNewArrayListWith1Entry() {
		final ArrayList<String> list = new ArrayList<>();
		list.add("value1");
		assertValuesMatch(asList("value1"), list);
	}
}
