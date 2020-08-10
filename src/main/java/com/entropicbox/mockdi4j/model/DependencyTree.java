package com.entropicbox.mockdi4j.model;

import java.util.HashSet;
import java.util.Set;

public class DependencyTree {

	private int count = 0;
	private Set<DependencyNode> roots;
	
	public DependencyTree(Set<Class<?>> initialSet) {
		this();
		if (initialSet == null)
			throw new NullPointerException();
		
		populateTree(initialSet);
	}

	public DependencyTree() {
		roots = new HashSet<>();
	}
	
	public int size() {
		return count;
	}

	public Set<DependencyNode> roots() {
		return roots;
	}

	private void populateTree(Set<Class<?>> initialSet)
	{
		for (Class<?> clazz : initialSet)
		{
			add(clazz);
		}
	}
	
	void add(Class<?> clazz)
	{
		this.count++;

		roots.add(new DependencyNode(clazz));
		
		for (DependencyNode root : roots)
		{
			//	DFS
		}
		
	}
}
