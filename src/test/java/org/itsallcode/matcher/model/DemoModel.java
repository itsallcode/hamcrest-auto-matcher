package org.itsallcode.matcher.model;

import java.util.Arrays;
import java.util.List;

import org.itsallcode.matcher.config.ConfigurableMatcher;

/**
 * A model class used for testing and demonstrating {@link ConfigurableMatcher}.
 */
public class DemoModel {
	private final int id;
	private final String name;
	private final DemoAttribute attr;
	private final List<DemoModel> children;
	private final String[] stringArray;
	private final Long longVal;

	public DemoModel(int id, String name, Long longVal, DemoAttribute attr, String[] stringArray,
			List<DemoModel> children) {
		this.id = id;
		this.name = name;
		this.longVal = longVal;
		this.attr = attr;
		this.stringArray = stringArray;
		this.children = children;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public DemoAttribute getAttr() {
		return attr;
	}

	public List<DemoModel> getChildren() {
		return children;
	}

	public String[] getStringArray() {
		return stringArray;
	}

	public Long getLongVal() {
		return longVal;
	}

	@Override
	public String toString() {
		return "DemoModel [id=" + id + ", name=" + name + ", attr=" + attr + ", children=" + children + ", stringArray="
				+ Arrays.toString(stringArray) + ", longVal=" + longVal + "]";
	}
}
