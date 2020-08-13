package com.entropicbox.mockdi4j.model;

import java.util.HashSet;
import java.util.Set;

public class DependencyNode {

	private Class<?> dependencyClass;
	private Set<DependencyNode> children;
	
	public DependencyNode(Class<?> dependencyClass) {
		if (dependencyClass == null)
			throw null;
		
		this.dependencyClass = dependencyClass;	
		
		this.children = new HashSet<>();
	}

	public Class<?> getDependencyClass() {
		return this.dependencyClass;
	}

	public void addChild(DependencyNode child) {
		if (child == null)
			throw null;
		
		children.add(child);
	}

	public Set<DependencyNode> children() {
		return children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dependencyClass == null) ? 0 : dependencyClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependencyNode other = (DependencyNode) obj;
		if (dependencyClass == null) {
			if (other.dependencyClass != null)
				return false;
		} else if (!dependencyClass.equals(other.dependencyClass))
			return false;
		return true;
	}
	
	

}
