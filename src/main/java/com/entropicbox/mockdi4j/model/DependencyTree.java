package com.entropicbox.mockdi4j.model;

import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import com.entropicbox.mockdi4j.util.ReflectionsUtils;

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
		// roots is a concurrent hash set,
		// that is important because we are adding and removing from roots within the
		// same iterator
		roots = ConcurrentHashMap.newKeySet();
	}

	public int size() {
		return count;
	}

	public Set<DependencyNode> roots() {
		return roots;
	}

	private void populateTree(Set<Class<?>> initialSet) {
		for (Class<?> clazz : initialSet) {
			add(clazz);
		}
	}

	public void add(Class<?> clazz) {
		DependencyNode newNode = new DependencyNode(clazz);

		roots.add(newNode);

		for (DependencyNode root : roots) {
			searchRootTreeAndLink(newNode, root);
		}

		this.count++;
	}

	private void searchRootTreeAndLink(DependencyNode newNode, DependencyNode root) {
		// Using DFS
		Stack<DependencyNode> stack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			DependencyNode currentNode = stack.pop();
			for (DependencyNode dependency : currentNode.children())
				stack.push(dependency);

			// For each node in the root, check and link the nodes
			checkAndLinkNodes(newNode, currentNode);
		}

	}

	private void checkAndLinkNodes(DependencyNode newNode, DependencyNode node) {
		if (ReflectionsUtils.classDependsOn(newNode.getDependencyClass(), node.getDependencyClass()))
			linkFromTo(node, newNode);
		else if (ReflectionsUtils.classDependsOn(node.getDependencyClass(), newNode.getDependencyClass()))
			linkFromTo(newNode, node);
	}

	private void linkFromTo(DependencyNode from, DependencyNode to) {
		to.addChild(from);
		roots.remove(from);
	}
}
