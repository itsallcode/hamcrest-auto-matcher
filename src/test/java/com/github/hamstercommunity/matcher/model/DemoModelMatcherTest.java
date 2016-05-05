/**
 * Automatic hamcrest matcher for model classes
 * Copyright (C) 2016 Christoph Pirkl <christoph at users.sourceforge.net>
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
package com.github.hamstercommunity.matcher.model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.github.hamstercommunity.matcher.config.ConfigurableMatcher;
import com.github.hamstercommunity.matcher.test.MatcherTestBase;

/**
 * This tests {@link ConfigurableMatcher} and demonstrates usage of
 * {@link MatcherTestBase}.
 */
public interface DemoModelMatcherTest extends MatcherTestBase<DemoModel> {

	static final int ID1 = 4711;
	static final int ID2 = 4242;
	static final int ID3 = 4243;
	static final String NAME1 = "name1";
	static final String NAME2 = "name2";
	static final String NAME3 = "name3";
	static final String ATTR1 = "attrValue1";
	static final String ATTR2 = "attrValue2";
	static final String ATTR3 = "attrValue3";

	static String SIMPLE_MODEL_DESCRIPTION = "{id=<" + ID1 + ">, name=\"" + NAME1
			+ "\", attr=null, stringArray=null, children=null}";
	static final String CHILD1 = "{id=<" + ID2 + ">, name=\"" + NAME2 + "\", attr={value=\"" + ATTR2
			+ "\"}, stringArray=null, children=an empty iterable}";
	static String COMPLEX_MODEL_DESCRIPTION = "{id=<" + ID1 + ">, name=\"" + NAME1 + "\", attr={value=\"" + ATTR1
			+ "\"}, stringArray=null, children=iterable containing [" + CHILD1 + "]}";

	@Test
	default void testMatchAllNull() {
		assertMatch(model(ID1, null, null, null));
	}

	@Test
	default void testMatchAttributeWithNullValue() {
		assertMatch(model(ID1, null, attr(null), null));
	}

	@Test
	default void testMatchEmptyList() {
		assertMatch(model(ID1, null, null, Collections.emptyList()));
	}

	@Test
	default void testMatchAllFieldsDefined() {
		assertMatch(model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR2), emptyList()))));
	}

	@Test
	default void testMessageSimpleDifferentId() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{id was <" + ID2 + ">}", //
				expectedSimpleModel(), //
				model(ID2, NAME1, null, null));
	}

	@Test
	default void testMessageSimpleDifferentName() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{name was \"" + NAME2 + "\"}", //
				expectedSimpleModel(), //
				model(ID1, NAME2, null, null));
	}

	@Test
	default void testMessageSimpleDifferentIdAndName() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{id was <" + ID2 + ">, name was \"" + NAME2 + "\"}", //
				expectedSimpleModel(), //
				model(ID2, NAME2, null, null));
	}

	@Test
	default void testMessageSimpleAttributeNull() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{attr was <DemoAttribute [value=null]>}", //
				expectedSimpleModel(), //
				model(ID1, NAME1, attr(null), null));
	}

	@Test
	default void testMessageSimpleAttributeNotNull() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{attr was <DemoAttribute [value=" + ATTR2 + "]>}", //
				expectedSimpleModel(), //
				model(ID1, NAME1, attr(ATTR2), null));
	}

	@Test
	default void testMessageSimpleEmptyChildList() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{children was <[]>}", //
				expectedSimpleModel(), //
				model(ID1, NAME1, null, emptyList()));
	}

	@Test
	default void testMessageSimpleFilledChildList() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{children was <[DemoModel [id=" + ID2 + ", name=null, attr=null, children=null, stringArray=null]]>}", //
				expectedSimpleModel(), //
				model(ID1, NAME1, null, asList(model(ID2, null, null, null))));
	}

	@Test
	default void testMessageComplexNullAttribute() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr was null}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, null, emptyList()))));
	}

	@Test
	default void testMessageComplexNullAttributeValue() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr {value was null}}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(null), emptyList()))));
	}

	@Test
	default void testMessageComplexDifferentAttributeValue() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {attr {value was \"" + ATTR3 + "\"}}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR3), emptyList()))));
	}

	@Test
	default void testMessageComplexChildDifferentId() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {id was <" + ID3 + ">}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID3, NAME2, attr(ATTR2), emptyList()))));
	}

	@Test
	default void testMessageComplexChildDifferentName() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {name was \"" + NAME3 + "\"}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME3, attr(ATTR2), emptyList()))));
	}

	@Test
	default void testMessageComplexChildNullName() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children item 0: {name was null}}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), asList(model(ID2, null, attr(ATTR2), emptyList()))));
	}

	@Test
	default void testMessageComplexEmptyChildList() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children No item matched: " + CHILD1 + "}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), emptyList()));
	}

	@Test
	default void testMessageComplexNullChildList() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children was null}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), null));
	}

	@Test
	default void testMessageComplexAdditionalChild() {
		assertFailureDescription(COMPLEX_MODEL_DESCRIPTION, //
				"{children Not matched: <DemoModel [id=" + ID3 + ", name=" + NAME3
						+ ", attr=null, children=null, stringArray=null]>}", //
				expectedComplexModel(), //
				model(ID1, NAME1, attr(ATTR1), //
						asList(model(ID2, NAME2, attr(ATTR2), emptyList()), //
								model(ID3, NAME3, null, null))));
	}

	@Test
	default void testMessageStringArrayEmpty() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was []}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[0]));
	}

	@Test
	default void testMessageStringArraySize1NullContent() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was [null]}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[1]));
	}

	@Test
	default void testMessageStringArraySize3NullContent() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was [null, null, null]}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[3]));
	}

	@Test
	default void testMessageStringArraySize1ValueContent() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was [\"val1\"]}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[] { "val1" }));
	}

	@Test
	default void testMessageStringArraySize3ValueContent() {
		assertFailureDescription(SIMPLE_MODEL_DESCRIPTION, //
				"{stringArray was [\"val1\", \"val2\", \"val3\"]}", //
				expectedSimpleModel(), //
				modelWithStringArray(new String[] { "val1", "val2", "val3" }));
	}

	static DemoModel expectedSimpleModel() {
		return model(ID1, NAME1, null, null);
	}

	static DemoModel expectedComplexModel() {
		return model(ID1, NAME1, attr(ATTR1), asList(model(ID2, NAME2, attr(ATTR2), emptyList())));
	}

	static DemoModel modelWithStringArray(String[] array) {
		return new DemoModel(ID1, NAME1, null, array, null);
	}

	static DemoModel model(int id, String name, DemoAttribute attribute, List<DemoModel> children) {
		return new DemoModel(id, name, attribute, null, children);
	}

	static DemoAttribute attr(String value) {
		return new DemoAttribute(value);
	}
}
