package com.github.hamstercommunity.matcher.auto;

import static com.github.hamstercommunity.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static com.github.hamstercommunity.matcher.auto.TestUtil.assertValuesMatch;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import com.github.hamstercommunity.matcher.model.DemoAttribute;

public class AutoMatcherListTest {

	@Test
	public void testEmptyList() {
		assertValuesDoNotMatch(emptyList(), singletonList("value2"));
	}

	@Test
	public void testIncompatibleMemberTypes() {
		assertValuesDoNotMatch(asList("string"), asList(1));
	}

	@Test
	public void testIncompatibleMemberTypesComplexTypes() {
		assertValuesDoNotMatch(asList(new DemoAttribute("attr")), asList(1));
	}

	@Test
	public void testEmptyListAndNewArrayList() {
		assertValuesMatch(emptyList(), new ArrayList<>());
	}

	@Test
	public void testEmptyListAndNewLinkedList() {
		assertValuesMatch(emptyList(), new LinkedList<>());
	}

	@Test
	public void testEmptyListAndAsListAreEqaul() {
		assertValuesMatch(emptyList(), asList());
	}

	@Test
	public void testSingletonList() {
		assertValuesDoNotMatch(singletonList("value1"), singletonList("value2"));
	}

	@Test
	public void testAsListWithDuplicateValue() {
		assertValuesDoNotMatch(asList("value1"), asList("value1", "value1"));
	}

	@Test
	public void testSingletonListAndNewArrayListWith1Entry() {
		final ArrayList<String> list = new ArrayList<>();
		list.add("value1");
		assertValuesMatch(singletonList("value1"), list);
	}

	@Test
	public void testAsListAndNewArrayListWith1Entry() {
		final ArrayList<String> list = new ArrayList<>();
		list.add("value1");
		assertValuesMatch(asList("value1"), list);
	}
}
