package org.itsallcode.matcher.auto;

import static org.itsallcode.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static org.itsallcode.matcher.auto.TestUtil.assertValuesMatch;

import org.junit.jupiter.api.Test;

class AutoMatcherArrayTest {

    @Test
    void testClassWithArrays() {
        final TestClass value1 = new TestClass((byte) 0xAA, new byte[] { (byte) 0xBB, (byte) 0xCC }, 0xDD,
                new int[] { 0xEE, 0xFF }, "0x11", new String[] { "0x22", "0x33" });
        final TestClass value2 = new TestClass((byte) 0x00, new byte[] { (byte) 0x00, (byte) 0x00 }, 0xDD,
                new int[] { 0x00, 0x00 }, "0x11", new String[] { "0x00", "0x00" });
        assertValuesDoNotMatch(value1, value2);
    }

    @Test
    void testClassWithArraysOnlyByteDiff() {
        final TestClass value1 = new TestClass((byte) 0xAA, new byte[0], 0xDD, new int[0], "0x11", new String[0]);
        final TestClass value2 = new TestClass((byte) 0xAB, new byte[0], 0xDD, new int[0], "0x11", new String[0]);
        assertValuesDoNotMatch(value1, value2);
    }

    @Test
    void testClassWithArraysOnlyIntDiff() {
        final TestClass value1 = new TestClass((byte) 0xAA, new byte[0], 0xDD, new int[0], "0x11", new String[0]);
        final TestClass value2 = new TestClass((byte) 0xAA, new byte[0], 0xDF, new int[0], "0x11", new String[0]);
        assertValuesDoNotMatch(value1, value2);
    }

    @Test
    void testClassWithArraysOnlyStringDiff() {
        final TestClass value1 = new TestClass((byte) 0xAA, new byte[0], 0xDD, new int[0], "0x11", new String[0]);
        final TestClass value2 = new TestClass((byte) 0xAA, new byte[0], 0xDD, new int[0], "0x12", new String[0]);
        assertValuesDoNotMatch(value1, value2);
    }

    @Test
    void testPrimitiveByteArrays() {
        assertValuesDoNotMatch(new byte[] { 1 }, new byte[] { 2 });
    }

    @Test
    void testPrimitiveCharArrays() {
        assertValuesDoNotMatch(new char[] { 1 }, new char[] { 2 });
    }

    @Test
    void testPrimitiveShortArrays() {
        assertValuesDoNotMatch(new short[] { 1 }, new short[] { 2 });
    }

    @Test
    void testPrimitiveBooleanArrays() {
        assertValuesDoNotMatch(new boolean[] { true }, new boolean[] { false });
    }

    @Test
    void testPrimitiveLongArrays() {
        assertValuesDoNotMatch(new long[] { 1 }, new long[] { 2 });
    }

    @Test
    void testPrimitiveIntArrays() {
        assertValuesDoNotMatch(new int[] { 1 }, new int[] { 2 });
    }

    @Test
    void testPrimitiveIntArraysDifferentSize() {
        assertValuesDoNotMatch(new int[] { 1, 2 }, new int[] { 3 });
    }

    @Test
    void testPrimitiveIntArraysEmpty() {
        assertValuesDoNotMatch(new int[] { 1, 2 }, new int[0]);
    }

    @Test
    void testPrimitiveFloatArray() {
        assertValuesDoNotMatch(new float[] { (float) 1.1 }, new float[] { (float) 2.2 });
    }

    @Test
    void testPrimitiveDoubleArray() {
        assertValuesDoNotMatch(new double[] { 1.1 }, new double[] { 2.2 });
    }

    @Test
    void testStringArray() {
        assertValuesDoNotMatch(new String[] { "a" }, new String[] { "b" });
    }

    @Test
    void testStringArrayEmpty() {
        assertValuesDoNotMatch(new String[] { "a" }, new String[0]);
    }

    @Test
    void testObjectArrayEmpty() {
        assertValuesDoNotMatch(new Object[] { "a" }, new Object[0]);
    }

    @Test
    void testObjectArrayMixed() {
        assertValuesDoNotMatch(new Object[] { "a", null, 1 }, new Object[0]);
    }

    @Test
    void testComplexObjectArrayMatch() {
        assertValuesMatch(new ArrayElement[] { new ArrayElement(1, "a") },
                new ArrayElement[] { new ArrayElement(1, "a") });
    }

    @Test
    void testComplexObjectArrayNoMatch() {
        assertValuesDoNotMatch(new ArrayElement[] { new ArrayElement(1, "a") },
                new ArrayElement[] { new ArrayElement(2, "b") });
    }

    @Test
    void testComplexObjectArrayEmpty() {
        assertValuesDoNotMatch(new ArrayElement[] { new ArrayElement(1, "a") }, new ArrayElement[0]);
    }

    static class ArrayElement {
        private final int id;
        private final String name;

        public ArrayElement(final int id, final String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ArrayElement [id=" + id + ", name=" + name + "]";
        }
    }

    static class TestClass {
        private final byte b;
        private final byte[] bA;
        private final int i;
        private final int[] iA;
        private final String s;
        private final String[] sA;

        public TestClass(final byte b, final byte[] bA, final int i, final int[] iA, final String s,
                final String[] sA) {
            this.b = b;
            this.bA = bA;
            this.i = i;
            this.iA = iA;
            this.s = s;
            this.sA = sA;
        }

        public byte getB() {
            return b;
        }

        public byte[] getbA() {
            return bA;
        }

        public int getI() {
            return i;
        }

        public int[] getiA() {
            return iA;
        }

        public String getS() {
            return s;
        }

        public String[] getsA() {
            return sA;
        }
    }
}
