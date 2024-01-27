package com.github.hamstercommunity.matcher.auto;

import static com.github.hamstercommunity.matcher.auto.TestUtil.assertValuesDoNotMatch;
import static com.github.hamstercommunity.matcher.auto.TestUtil.assertValuesMatch;

import org.junit.Test;

public class AutoMatcherArrayTest {

	@Test
	public void testClassWithArrays() {
		final TestClass value1 = new TestClass((byte) 0xAA, new byte[] { (byte) 0xBB, (byte) 0xCC }, 0xDD,
				new int[] { 0xEE, 0xFF }, "0x11", new String[] { "0x22", "0x33" });
		final TestClass value2 = new TestClass((byte) 0x00, new byte[] { (byte) 0x00, (byte) 0x00 }, 0xDD,
				new int[] { 0x00, 0x00 }, "0x11", new String[] { "0x00", "0x00" });
		assertValuesDoNotMatch(value1, value2);
	}

	@Test
	public void testClassWithArraysOnlyByteDiff() {
		final TestClass value1 = new TestClass((byte) 0xAA, new byte[0], 0xDD, new int[0], "0x11", new String[0]);
		final TestClass value2 = new TestClass((byte) 0xAB, new byte[0], 0xDD, new int[0], "0x11", new String[0]);
		assertValuesDoNotMatch(value1, value2);
	}

	@Test
	public void testClassWithArraysOnlyIntDiff() {
		final TestClass value1 = new TestClass((byte) 0xAA, new byte[0], 0xDD, new int[0], "0x11", new String[0]);
		final TestClass value2 = new TestClass((byte) 0xAA, new byte[0], 0xDF, new int[0], "0x11", new String[0]);
		assertValuesDoNotMatch(value1, value2);
	}

	@Test
	public void testClassWithArraysOnlyStringDiff() {
		final TestClass value1 = new TestClass((byte) 0xAA, new byte[0], 0xDD, new int[0], "0x11", new String[0]);
		final TestClass value2 = new TestClass((byte) 0xAA, new byte[0], 0xDD, new int[0], "0x12", new String[0]);
		assertValuesDoNotMatch(value1, value2);
	}

	@Test
	public void testPrimitiveByteArrays() {
		assertValuesDoNotMatch(new byte[] { 1 }, new byte[] { 2 });
	}

	@Test
	public void testPrimitiveCharArrays() {
		assertValuesDoNotMatch(new char[] { 1 }, new char[] { 2 });
	}

	@Test
	public void testPrimitiveShortArrays() {
		assertValuesDoNotMatch(new short[] { 1 }, new short[] { 2 });
	}

	@Test
	public void testPrimitiveBooleanArrays() {
		assertValuesDoNotMatch(new boolean[] { true }, new boolean[] { false });
	}

	@Test
	public void testPrimitiveLongArrays() {
		assertValuesDoNotMatch(new long[] { 1 }, new long[] { 2 });
	}

	@Test
	public void testPrimitiveIntArrays() {
		assertValuesDoNotMatch(new int[] { 1 }, new int[] { 2 });
	}

	@Test
	public void testPrimitiveIntArraysDifferentSize() {
		assertValuesDoNotMatch(new int[] { 1, 2 }, new int[] { 3 });
	}

	@Test
	public void testPrimitiveIntArraysEmpty() {
		assertValuesDoNotMatch(new int[] { 1, 2 }, new int[0]);
	}

	@Test
	public void testPrimitiveFloatArray() {
		assertValuesDoNotMatch(new float[] { (float) 1.1 }, new float[] { (float) 2.2 });
	}

	@Test
	public void testPrimitiveDoubleArray() {
		assertValuesDoNotMatch(new double[] { 1.1 }, new double[] { 2.2 });
	}

	@Test
	public void testStringArray() {
		assertValuesDoNotMatch(new String[] { "a" }, new String[] { "b" });
	}

	@Test
	public void testStringArrayEmpty() {
		assertValuesDoNotMatch(new String[] { "a" }, new String[0]);
	}

	@Test
	public void testComplexObjectArrayMatch() {
		assertValuesMatch(new ArrayElement[] { new ArrayElement(1, "a") },
				new ArrayElement[] { new ArrayElement(1, "a") });
	}

	@Test
	public void testComplexObjectArrayNoMatch() {
		assertValuesDoNotMatch(new ArrayElement[] { new ArrayElement(1, "a") },
				new ArrayElement[] { new ArrayElement(2, "b") });
	}

	@Test
	public void testComplexObjectArrayEmpty() {
		assertValuesDoNotMatch(new ArrayElement[] { new ArrayElement(1, "a") }, new ArrayElement[0]);
	}

	static class ArrayElement {
		private final int id;
		private final String name;

		public ArrayElement(int id, String name) {
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

		public TestClass(byte b, byte[] bA, int i, int[] iA, String s, String[] sA) {
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
