package com.github.hamstercommunity.matcher.model;

import java.util.List;

public class DemoModel {
	private final int id;
	private final String name;
	private final DemoAttribute attr;
	private final List<DemoModel> children;

	public DemoModel(int id, String name, DemoAttribute attr, List<DemoModel> children) {
		this.id = id;
		this.name = name;
		this.attr = attr;
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

	@Override
	public String toString() {
		return "DemoModel [id=" + id + ", name=" + name + ", attr=" + attr + ", children=" + children + "]";
	}
}
