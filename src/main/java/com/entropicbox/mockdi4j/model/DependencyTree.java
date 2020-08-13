package com.entropicbox.mockdi4j.model;

import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import com.entropicbox.mockdi4j.exception.CircularDependencyException;
import com.entropicbox.mockdi4j.exception.DuplicateDependencyException;
import com.entropicbox.mockdi4j.util.ReflectionsUtils;

public class DependencyTree {

	private int count = 0;
	private Set<DependencyNode> roots;

	public DependencyTree(Set<Class<?>> initialSet) {
		this();
		if (initialSet == null)
			throw null;

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
		//	Do not allow non-instantiable classes to be added to the dependency tree
		if (!ReflectionsUtils.isInstantiable(clazz)) {
			return;
		}
		
		try
		{
			checkForDuplicateDependencies(clazz);

			DependencyNode newNode = new DependencyNode(clazz);
	
			roots.add(newNode);
	
			for (DependencyNode root : roots) {
				searchRootTreeAndLink(newNode, root);
			}
							
			this.count++;
		}
		catch (CircularDependencyException | DuplicateDependencyException e)
		{
			roots.clear();
			throw e;
		}
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
		if (ReflectionsUtils.classDependsOn(node.getDependencyClass(), newNode.getDependencyClass()) 
				&& ReflectionsUtils.classDependsOn(newNode.getDependencyClass(), node.getDependencyClass()))
			throw new CircularDependencyException();

		
		if (ReflectionsUtils.classDependsOn(newNode.getDependencyClass(), node.getDependencyClass()))
			linkFromTo(node, newNode);
		if (ReflectionsUtils.classDependsOn(node.getDependencyClass(), newNode.getDependencyClass()))
			linkFromTo(newNode, node);
	}

	private void linkFromTo(DependencyNode from, DependencyNode to) {
		to.addChild(from);
		roots.remove(from);
		checkForCircularDependency(from);
	}
	
	private void checkForCircularDependency(DependencyNode node) {
		//	Using DFS starting at this node's children...
		Stack<DependencyNode> stack = new Stack<>();
		
		for (DependencyNode dependency : node.children())
			stack.push(dependency);
		
		while (!stack.isEmpty()) {
			DependencyNode currentNode = stack.pop();
			for (DependencyNode dependency : currentNode.children())
				stack.push(dependency);
			
			//	If we loop back around to the node we are checking for, 
			//	then we have a circular dependency
			if (currentNode.equals(node)) {
				throw new CircularDependencyException();
			}
		}
	}

	private void checkForDuplicateDependencies(Class<?> clazz) {		
		for (DependencyNode root : roots)
		{
			
			//	Using DFS starting at this node's children...
			Stack<DependencyNode> stack = new Stack<>();
			stack.push(root);
			
			while (!stack.isEmpty()) {
				DependencyNode currentNode = stack.pop();
				for (DependencyNode dependency : currentNode.children())
					stack.push(dependency);
				
				//	If we loop back around to the node we are checking for, 
				//	then we have a circular dependency
				if (currentNode.getDependencyClass().equals(clazz)) {
					throw new DuplicateDependencyException();
				}
			}
		}
	}

}
