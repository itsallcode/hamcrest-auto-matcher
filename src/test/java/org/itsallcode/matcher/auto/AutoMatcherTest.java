package org.itsallcode.matcher.auto;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesMatch;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import org.hamcrest.Matcher;
import org.itsallcode.matcher.model.DemoAttribute;
import org.itsallcode.matcher.model.DemoModel;
import org.junit.Before;
import org.junit.Test;

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
	@SuppressWarnings("unchecked")
	public void testIncompatibleTypes() {
		final Matcher<?> matcher = (Matcher<?>) AutoMatcher.equalTo(value1);
		assertThrows(
				"Expected object of type " + DemoModel.class.getName() + " but got " + Integer.class.getName() + ": 1",
				AssertionError.class,
				() -> assertThat(1, (Matcher<Integer>) matcher));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testIncompatibleListMemberTypes() {
		final Object actual = asList(1);
		final Object expected = asList(new DemoAttribute("attr"));
		final Matcher<?> matcher = (Matcher<?>) AutoMatcher.equalTo(expected);
		assertThrows(
				"Expected object of type " + DemoAttribute.class.getName() + " but got "
						+ Integer.class.getName() + ": 1",
				AssertionError.class,
				() -> assertThat(actual, (Matcher<Object>) matcher));
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

	@Test
	public void testStringListContains() {
		assertThat(asList("val"), not(AutoMatcher.contains("wrong")));
		assertThat(asList("val"), AutoMatcher.contains("val"));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeString() {
		assertValuesDoNotMatch("val", "wrong");
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeByte() {
		assertValuesDoNotMatch((byte) 1, (byte) 2);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeInteger() {
		assertValuesDoNotMatch(1, 2);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeLong() {
		assertValuesDoNotMatch(1L, 2L);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeBoolean() {
		assertValuesDoNotMatch(true, false);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeFloat() {
		assertValuesDoNotMatch(1.0F, 1.1F);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeDouble() {
		assertValuesDoNotMatch(1.0D, 1.1D);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeShort() {
		assertValuesDoNotMatch((short) 1, (short) 2);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeChar() {
		assertValuesDoNotMatch('a', 'b');
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeBigInteger() {
		assertValuesDoNotMatch(BigInteger.ZERO, BigInteger.ONE);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeBigDecimal() {
		assertValuesDoNotMatch(BigDecimal.ZERO, BigDecimal.ONE);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeCalendar() {
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.MILLISECOND, 1);
		assertValuesDoNotMatch(cal1, cal2);
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeDate() {
		assertValuesDoNotMatch(new Date(1), new Date(2));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeSqlDate() {
		assertValuesDoNotMatch(new java.sql.Date(1), new java.sql.Date(2));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeSqlTimestamp() {
		assertValuesDoNotMatch(new java.sql.Timestamp(1), new java.sql.Timestamp(2));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeInstance() {
		assertValuesDoNotMatch(Instant.ofEpochMilli(1), Instant.ofEpochMilli(2));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeLocalDate() {
		assertValuesDoNotMatch(LocalDate.parse("2024-01-31"), LocalDate.parse("2024-02-01"));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeFile() {
		assertValuesDoNotMatch(new File("a"), new File("b"));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypePath() {
		assertValuesDoNotMatch(Paths.get("a"), Paths.get("b"));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeUuid() {
		assertValuesDoNotMatch(UUID.randomUUID(), UUID.randomUUID());
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeUrl() throws MalformedURLException {
		assertValuesDoNotMatch(new URL("http://example.com"), new URL("http://example.com/test"));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeUri() {
		assertValuesDoNotMatch(URI.create("http://example.com"), URI.create("http://example2.com"));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeOptionalEmpty() {
		assertValuesDoNotMatch(Optional.empty(), Optional.of("a"),
				equalTo("\nExpected: has value that is \"a\"\n     but: was <Empty>"));
		assertValuesDoNotMatch(Optional.of("a"), Optional.empty(),
				equalTo("\nExpected: is <Empty>\n     but: had value \"a\""));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeOptionalWithString() {
		assertValuesDoNotMatch(Optional.of("b"), Optional.of("a"),
				equalTo("\nExpected: has value that is \"a\"\n     but: value was \"b\""));
	}

	@Test
	public void testAutoMatcherWorksForSimpleTypeOptionalWithModel() {
		final DemoModel m1 = new DemoModel(1, "a", 1L, null, null, null);
		final DemoModel m2 = new DemoModel(2, "a", 1L, null, null, null);
		assertValuesDoNotMatch(Optional.of(m1), Optional.of(m2), containsString("but: value {id was <1>}"));
		assertValuesMatch(Optional.of(value1), Optional.of(value1Equal));
	}

	private DemoModel model(final String name, final int id) {
		return model(name, id,
				asList(model(name + "-child1", id, emptyList()), model(name + "-child2", id, emptyList())));
	}

	private DemoModel model(final String name, final int id, final List<DemoModel> children) {
		return new DemoModel(id, name + "-" + id, id * 2L, attr(name + "-attr-" + id),
				new String[] { name + "-item1-" + id, name + "-item2-" + id }, children);
	}

	private DemoAttribute attr(final String value) {
		return new DemoAttribute(value);
	}
}
