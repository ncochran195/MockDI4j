package com.entropicbox.mockdi4j.model;

import java.util.HashSet;
import java.util.Set;

public class DependencyNode {

	private Class<?> dependencyClass;
	private Set<DependencyNode> children;
	
	public DependencyNode(Class<?> dependencyClass) {
		if (dependencyClass == null)
			throw new NullPointerException();
		
		this.dependencyClass = dependencyClass;	
		
		this.children = new HashSet<>();
	}

	public Class<?> getDependencyClass() {
		return this.dependencyClass;
	}

	public void addChild(DependencyNode child) {
		if (child == null)
			throw new NullPointerException();
		
		children.add(child);
	}

	public Set<DependencyNode> children() {
		return children;
	}

}
