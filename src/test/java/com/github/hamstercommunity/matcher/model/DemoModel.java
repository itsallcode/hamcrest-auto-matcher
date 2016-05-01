package com.github.hamstercommunity.matcher.model;

import java.util.List;

public class DemoModel {
	private final int id;
	private final String name;
	private final DemoAttribute attribute;
	private final List<DemoModel> children;

	public DemoModel(int id, String name, DemoAttribute attribute, List<DemoModel> children) {
		this.id = id;
		this.name = name;
		this.attribute = attribute;
		this.children = children;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public DemoAttribute getAttribute() {
		return attribute;
	}

	public List<DemoModel> getChildren() {
		return children;
	}
}
