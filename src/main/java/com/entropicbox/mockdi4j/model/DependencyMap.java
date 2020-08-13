package com.entropicbox.mockdi4j.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.entropicbox.mockdi4j.exception.DependencyNotFoundException;
import com.entropicbox.mockdi4j.exception.UnsatisfiedDependencyException;
import com.entropicbox.mockdi4j.exception.WireDependencyException;
import com.entropicbox.mockdi4j.util.ReflectionsUtils;

public class DependencyMap {

	private final DependencyTree tree;

	private Map<Class<?>, Object> instanceMap = new HashMap<>();

	public DependencyMap(DependencyTree tree) {
		if (tree == null)
			throw null;
		this.tree = tree;
	}

	public <T> T get(Class<T> clazz) {
		return this.getFromInstanceMap(clazz);
	}

	public void wire() {
		for (DependencyNode root : tree.roots()) {
			wireNode(root);
		}
	}

	public void wireNode(DependencyNode node) {
		try {
			// If we already have a node wired, ignore...
			get(node.getDependencyClass());
		} catch (DependencyNotFoundException e) {
			// Otherwise, go ahead and wire the node
			wireNotWiredNode(node);
		}
	}

	private void wireNotWiredNode(DependencyNode node) {
		Constructor<?> maxArgsConstructor = ReflectionsUtils.getMaxArgsConstructor(node.getDependencyClass());

		// Found a leaf, termination case
		if (maxArgsConstructor.getParameterCount() == 0) {
			wireLeafInstance(node);
			return;
		}

		// DFS through tree recursively
		for (DependencyNode child : node.children()) {
			wireNode(child);
		}

		// non-leaf instance
		wireNonLeafInstance(node, maxArgsConstructor);
	}

	private void wireLeafInstance(DependencyNode node) {
		instanceMap.put(node.getDependencyClass(), ReflectionsUtils.newLeafInstance(node.getDependencyClass()));
	}

	private void wireNonLeafInstance(DependencyNode node, Constructor<?> constructor) {
		// Get max args constructor
		Object[] paramsInstances = getWiredParamsFromInstanceMap(constructor);

		try {
			// Create new instance from constructor, using instances
			Object instance = constructor.newInstance(paramsInstances);
			// add to map
			instanceMap.put(node.getDependencyClass(), instance);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			throw new WireDependencyException();
		}
	}

	private Object[] getWiredParamsFromInstanceMap(Constructor<?> constructor) {
		Object[] paramsInstances = new Object[constructor.getParameterCount()];

		for (int i = 0; i < constructor.getParameterCount(); i++) {
			try {
				Object childInstance = get(constructor.getParameterTypes()[i]);
				paramsInstances[i] = childInstance;
			} catch (DependencyNotFoundException e) {
				throw new UnsatisfiedDependencyException();

			}
		}

		return paramsInstances;
	}

	@SuppressWarnings("unchecked")
	private <T> T getFromInstanceMap(Class<T> clazz) {
		for (Class<?> instanceClass : instanceMap.keySet()) {
			if (clazz.isAssignableFrom(instanceClass)) {
				return (T) instanceMap.get(instanceClass);
			}
		}

		throw new DependencyNotFoundException();
	}

}
