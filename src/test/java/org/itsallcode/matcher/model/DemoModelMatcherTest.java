package org.itsallcode.matcher.model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.List;

import org.itsallcode.matcher.config.ConfigurableMatcher;
import org.itsallcode.matcher.test.MatcherTestBase;
import org.junit.jupiter.api.Test;

/**
 * This tests {@link ConfigurableMatcher} and demonstrates usage of
 * {@link MatcherTestBase}.
 */
abstract class DemoModelMatcherTest extends MatcherTestBase<DemoModel> {

	static final int ID1 = 4711;
	static final int ID2 = 4242;
	static final int ID3 = 4243;
	static final String NAME1 = "name1";
	static final String NAME2 = "name2";
	static final String NAME3 = "name3";
	static final String ATTR1 = "attrValue1";
	static final String ATTR2 = "attrValue2";
	static final String ATTR3 = "attrValue3";

	static String SIMPLE_MODEL_DESCRIPTION = "{id=<" + ID1 + ">, longVal=null, name=\"" + NAME1
			+ "\", attr=null, children=null, stringArray=null}";
	static final String CHILD1 = "{id=<" + ID2 + ">, longVal=null, name=\"" + NAME2 + "\", attr={value=\"" + ATTR2
			+ "\"}, children=an empty iterable, stringArray=null}";
	static String COMPLEX_MODEL_DESCRIPTION = "{id=<" + ID1 + ">, longVal=null, name=\"" + NAME1 + "\", attr={value=\""
			+ ATTR1 + "\"}, children=iterable containing [" + CHILD1 + "], stringArray=null}";

	@Test
	void testMatchAllNull() {
		assertMatch(model(ID1, null, null, null));
	}

	@Test
	void testMatchAttributeWithNullValue() {
		assertMatch(model(ID1, null, attr(null), null));
	}

	@Test
	void testMatchEmptyList() {
		assertMatch(model(ID1, null, null, Collections.emptyList()));
	}

	@Test
	void testMatchAllFieldsDefined() {
		assertMatch(model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR2), emptyList()))));
	}

	@Test
	void testMessageSimpleDifferentId() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{id was <" + ID2 + ">}", //
				expectedSimpleModel(), //
				model(ID2, NAME1, null, null));
	}

	@Test
	void testMessageSimpleDifferentName() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{name was \"" + NAME2 + "\"}", //
				expectedSimpleModel(), //
				model(ID1, NAME2, null, null));
	}

	@Test
	void testMessageSimpleDifferentIdAndName() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{id was <" + ID2 + ">, name was \"" + NAME2 + "\"}", //
				expectedSimpleModel(), //
				model(ID2, NAME2, null, null));
	}

	@Test
	void testMessageSimpleAttributeNull() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{attr was <DemoAttribute [value=null]>}", //
				expectedSimpleModel(), //
				model(ID1, NAME1, attr(null), null));
	}

	@Test
	void testMessageSimpleAttributeNotNull() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{attr was <DemoAttribute [value=" + ATTR2 + "]>}", //
				expectedSimpleModel(), //
				model(ID1, NAME1, attr(ATTR2), null));
	}

	@Test
	void testMessageSimpleEmptyChildList() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{children was <[]>}", //
				expectedSimpleModel(), //
				model(ID1, NAME1, null, emptyList()));
	}

	@Test
	void testMessageSimpleFilledChildList() {
		final List<DemoModel> actualChildren = asList(model(ID2, null, null, null));
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{children was <" + actualChildren.toString() + ">}", //
				expectedSimpleModel(), //
				model(ID1, NAME1, null, actualChildren));
	}

	@Test
	void testMessageComplexNullAttribute() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr was null}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, null, emptyList()))));
	}

	@Test
	void testMessageComplexNullAttributeValue() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr {value was null}}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(null), emptyList()))));
	}

	@Test
	void testMessageComplexDifferentAttributeValue() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr {value was \"" + ATTR3 + "\"}}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR3), emptyList()))));
	}

	@Test
	void testMessageComplexChildDifferentId() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {id was <" + ID3 + ">}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID3, NAME2, attr(ATTR2), emptyList()))));
	}

	@Test
	void testMessageComplexChildDifferentName() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {name was \"" + NAME3 + "\"}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME3, attr(ATTR2), emptyList()))));
	}

	@Test
	void testMessageComplexChildNullName() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {name was null}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, null, attr(ATTR2), emptyList()))));
	}

	@Test
	void testMessageComplexEmptyChildList() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children No item matched: " + CHILD1 + "}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), emptyList()));
	}

	@Test
	void testMessageComplexNullChildList() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children was null}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), null));
	}

	@Test
	void testMessageComplexAdditionalChild() {
		final DemoModel unmatchedChild = model(ID3, NAME3, null, null);
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children Not matched: <" + unmatchedChild.toString() + ">}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), //
						asList(model(ID2, NAME2, attr(ATTR2), emptyList()), //
								unmatchedChild)));
	}

	@Test
	void testMessageStringArrayEmpty() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was []}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[0]));
	}

	@Test
	void testMessageStringArraySize1NullContent() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was [null]}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[1]));
	}

	@Test
	void testMessageStringArraySize3NullContent() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was [null, null, null]}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[3]));
	}

	@Test
	void testMessageStringArraySize1ValueContent() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was [\"val1\"]}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[] { "val1" }));
	}

	@Test
	void testMessageStringArraySize3ValueContent() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was [\"val1\", \"val2\", \"val3\"]}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[] { "val1", "val2", "val3" }));
	}

	@Test
	void testMessageLongValue() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{longVal was <42L>}", //
				expectedSimpleModel(), //
				modelWithLongVal(42L));
	}

	static DemoModel expectedSimpleModel() {
		return model(ID1, NAME1, null, null);
	}

	static DemoModel expectedComplexModel() {
		return model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR2), emptyList())));
	}

	static DemoModel modelWithStringArray(final String[] array) {
		return new DemoModel(ID1, NAME1, null, null, array, null);
	}

	static DemoModel modelWithLongVal(final Long val) {
		return new DemoModel(ID1, NAME1, val, null, null, null);
	}

	static DemoModel model(final int id, final String name, final DemoAttribute attribute,
			final List<DemoModel> children) {
		return new DemoModel(id, name, null, attribute, null, children);
	}

	static DemoAttribute attr(final String value) {
		return new DemoAttribute(value);
	}
}
