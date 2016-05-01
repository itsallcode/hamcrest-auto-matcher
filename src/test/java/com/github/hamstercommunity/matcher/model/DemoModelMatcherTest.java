package com.github.hamstercommunity.matcher.model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.github.hamstercommunity.matcher.test.MatcherTestBase;

public class DemoModelMatcherTest extends MatcherTestBase<DemoModel> {

	private static final int ID1 = 4711;
	private static final int ID2 = 4242;
	private static final String NAME1 = "name1";
	private static final String NAME2 = "name2";
	private static final String ATTR1 = "attrValue1";
	private static final String ATTR2 = "attrValue2";

	@Test
	public void testMatchAllNull() {
		assertMatch(model(ID1, null, null, null));
	}

	@Test
	public void testMatchAttributeWithNullValue() {
		assertMatch(model(ID1, null, attr(null), null));
	}

	@Test
	public void testMatchEmptyList() {
		assertMatch(model(ID1, null, null, emptyList()));
	}

	@Test
	public void testMatchAllFieldsDefined() {
		assertMatch(model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR2), emptyList()))));
	}

	@Override
	protected Matcher<? super DemoModel> createMatcher(DemoModel expected) {
		return DemoModelMatcher.equalTo(expected);
	}

	private DemoModel model(int id, String name, DemoAttribute attribute, List<DemoModel> children) {
		return new DemoModel(id, name, attribute, children);
	}

	private DemoAttribute attr(String value) {
		return new DemoAttribute(value);
	}
}
