package org.itsallcode.matcher.auto;

import static java.util.Collections.emptySet;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesMatch;

import java.util.*;

import org.itsallcode.matcher.model.DemoAttribute;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class AutoMatcherSetTest {

    @Test
    void testEmptySet() {
        assertValuesDoNotMatch(emptySet(), Set.of("value2"));
    }

    @Test
    void testIncompatibleMemberTypes() {
        assertValuesDoNotMatch(Set.of("string"), Set.of(1));
    }

    @Test
    void testIncompatibleMemberTypesComplexTypes() {
        assertValuesDoNotMatch(Set.of(new DemoAttribute("attr")), Set.of(1));
    }

    @Test
    void testEmptySetAndNewHashSetList() {
        assertValuesMatch(emptySet(), new HashSet<>());
    }

    @Test
    void testEmptySetAndNewTreeSet() {
        assertValuesMatch(emptySet(), new TreeSet<>());
    }

    @Test
    void testEmptySetAndAsSetOfAreEqual() {
        assertValuesMatch(emptySet(), Set.of());
    }

    @RepeatedTest(name = "ignores set order repetition {currentRepetition} of {totalRepetitions}", value = 20)
    void ignoresSetOrder() {
        final long value = System.nanoTime();
        final String value1 = "value" + value;
        final String value2 = "value2" + (value + 1);
        final Set<String> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        assertValuesMatch(Set.of(value1, value2), set);
    }

    @Test
    void ignoresSetOrderForLinkedHashSet() {
        final Set<String> set1 = new LinkedHashSet<>(List.of("a", "b"));
        final Set<String> set2 = new LinkedHashSet<>(List.of("b", "a"));
        assertValuesMatch(set1, set2);
    }
}
