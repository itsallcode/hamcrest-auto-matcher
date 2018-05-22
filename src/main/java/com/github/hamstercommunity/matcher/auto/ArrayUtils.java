/**
 * Automatic hamcrest matcher for model classes
 * Copyright (C) 2017 Christoph Pirkl <christoph at users.sourceforge.net>
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
package com.github.hamstercommunity.matcher.auto;

import java.util.Arrays;

class ArrayUtils {

	static Object[] convertPrimitiveArray(Object array) {
		final Class<?> componentType = array.getClass().getComponentType();
		if (Byte.TYPE.equals(componentType)) {
			return convertByteArray((byte[]) array);
		}
		if (Short.TYPE.equals(componentType)) {
			return convertShortArray((short[]) array);
		}
		if (Float.TYPE.equals(componentType)) {
			return convertFloatArray((float[]) array);
		}
		if (Boolean.TYPE.equals(componentType)) {
			return convertBooleanArray((boolean[]) array);
		}
		if (Character.TYPE.equals(componentType)) {
			return convertCharacterArray((char[]) array);
		}
		if (Integer.TYPE.equals(componentType)) {
			return Arrays.stream((int[]) array).boxed().toArray();
		}
		if (Long.TYPE.equals(componentType)) {
			return Arrays.stream((long[]) array).boxed().toArray();
		}
		if (Double.TYPE.equals(componentType)) {
			return Arrays.stream((double[]) array).boxed().toArray();
		}
		throw new IllegalArgumentException(
				"Unsupported component type " + componentType.getName() + " for array " + array);
	}

	private static Object[] convertByteArray(byte[] array) {
		final Byte[] objectArray = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			objectArray[i] = array[i];
		}
		return objectArray;
	}

	private static Object[] convertFloatArray(float[] array) {
		final Float[] objectArray = new Float[array.length];
		for (int i = 0; i < array.length; i++) {
			objectArray[i] = array[i];
		}
		return objectArray;
	}

	private static Object[] convertBooleanArray(boolean[] array) {
		final Boolean[] objectArray = new Boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			objectArray[i] = array[i];
		}
		return objectArray;
	}

	private static Object[] convertCharacterArray(char[] array) {
		final Character[] objectArray = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			objectArray[i] = array[i];
		}
		return objectArray;
	}

	private static Object[] convertShortArray(short[] array) {
		final Short[] objectArray = new Short[array.length];
		for (int i = 0; i < array.length; i++) {
			objectArray[i] = array[i];
		}
		return objectArray;
	}
}
