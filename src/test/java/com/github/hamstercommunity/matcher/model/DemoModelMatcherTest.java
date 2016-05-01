package com.github.hamstercommunity.matcher.model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import com.github.hamstercommunity.matcher.test.MatcherTestBase;

public class DemoModelMatcherTest extends MatcherTestBase<DemoModel> {

	private static final int ID1 = 4711;
	private static final int ID2 = 4242;
	private static final int ID3 = 4243;
	private static final String NAME1 = "name1";
	private static final String NAME2 = "name2";
	private static final String NAME3 = "name3";
	private static final String ATTR1 = "attrValue1";
	private static final String ATTR2 = "attrValue2";
	private static final String ATTR3 = "attrValue3";

	private static String SIMPLE_MODEL_DESCRIPTION = "{id=<" + ID1 + ">, name=\"" + NAME1
			+ "\", attr=null, children=null}";
	private static final String CHILD1 = "{id=<" + ID2 + ">, name=\"" + NAME2 + "\", attr={value=\"" + ATTR2
			+ "\"}, children=an empty iterable}";
	private static String COMPLEX_MODEL_DESCRIPTION = "{id=<" + ID1 + ">, name=\"" + NAME1 + "\", attr={value=\""
			+ ATTR1 + "\"}, children=iterable containing [" + CHILD1 + "]}";

	// java.lang.AssertionError:
	// Expected: (an instance of java.lang.AssertionError and exception with
	// message a string containing "Expected: {id=<4711>, name=\"name1\",
	// attr=null, children=null}\n but: {children was <[DemoModel [id=4242,
	// name=null, attr=null, children=null]]>}")
	// but: exception with message a string containing "Expected: {id=<4711>,
	// name=\"name1\", attr=null, children=null}\n but: {children was
	// <[DemoModel [id=4242, name=null, attr=null, children=null]]>}" message
	// was "
	// Expected: {id=<4711>, name="name1", attr={value="attrValue1"},
	// children=iterable containing [{id=<4242>, name="name2",
	// attr={value="attrValue2"}, children=an empty iterable}]}
	// but: {children item 0: {attr was null}}"
	// Stacktrace was: java.lang.AssertionError:
	// Expected: {id=<4711>, name="name1", attr={value="attrValue1"},
	// children=iterable containing [{id=<4242>, name="name2",
	// attr={value="attrValue2"}, children=an empty iterable}]}

	private DemoModel expectedSimpleModel;
	private DemoModel expectedComplexModel;

	@Before
	public void setup() {
		expectedSimpleModel = model(ID1, NAME1, null, null);
		expectedComplexModel = model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR2), emptyList())));
	}

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

	@Test
	public void testMessageSimpleDifferentId() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{id was <" + ID2 + ">}", //
				expectedSimpleModel, //
				model(ID2, NAME1, null, null));
	}

	@Test
	public void testMessageSimpleDifferentName() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{name was \"" + NAME2 + "\"}", //
				expectedSimpleModel, //
				model(ID1, NAME2, null, null));
	}

	@Test
	public void testMessageSimpleDifferentIdAndName() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{id was <" + ID2 + ">, name was \"" + NAME2 + "\"}", //
				expectedSimpleModel, //
				model(ID2, NAME2, null, null));
	}

	@Test
	public void testMessageSimpleAttributeNull() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{attr was <DemoAttribute [value=null]>}", //
				expectedSimpleModel, //
				model(ID1, NAME1, attr(null), null));
	}

	@Test
	public void testMessageSimpleAttributeNotNull() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{attr was <DemoAttribute [value=" + ATTR2 + "]>}", //
				expectedSimpleModel, //
				model(ID1, NAME1, attr(ATTR2), null));
	}

	@Test
	public void testMessageSimpleEmptyChildList() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{children was <[]>}", //
				expectedSimpleModel, //
				model(ID1, NAME1, null, emptyList()));
	}

	@Test
	public void testMessageSimpleFilledChildList() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{children was <[DemoModel [id=" + ID2 + ", name=null, attr=null, children=null]]>}", //
				expectedSimpleModel, //
				model(ID1, NAME1, null, asList(model(ID2, null, null, null))));
	}

	@Test
	public void testMessageComplexNullAttribute() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr was null}}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, null, emptyList()))));
	}

	@Test
	public void testMessageComplexNullAttributeValue() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr {value was null}}}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(null), emptyList()))));
	}

	@Test
	public void testMessageComplexDifferentAttributeValue() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr {value was \"" + ATTR3 + "\"}}}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR3), emptyList()))));
	}

	@Test
	public void testMessageComplexChildDifferentId() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {id was <" + ID3 + ">}}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID3, NAME2, attr(ATTR2), emptyList()))));
	}

	@Test
	public void testMessageComplexChildDifferentName() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {name was \"" + NAME3 + "\"}}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME3, attr(ATTR2), emptyList()))));
	}

	@Test
	public void testMessageComplexChildNullName() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {name was null}}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, null, attr(ATTR2), emptyList()))));
	}

	@Test
	public void testMessageComplexEmptyChildList() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children No item matched: " + CHILD1 + "}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), emptyList()));
	}

	@Test
	public void testMessageComplexNullChildList() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children was null}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), null));
	}

	@Test
	public void testMessageComplexAdditionalChild() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children Not matched: <DemoModel [id=" + ID3 + ", name=" + NAME3 + ", attr=null, children=null]>}", //
				expectedComplexModel, //
				model(ID1, NAME1, attr(ATTR1), //
						asList(model(ID2, NAME2, attr(ATTR2), emptyList()), //
								model(ID3, NAME3, null, null))));
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