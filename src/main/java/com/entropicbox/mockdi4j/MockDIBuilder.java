package com.entropicbox.mockdi4j;

import java.util.HashSet;
import java.util.Set;

import com.entropicbox.mockdi4j.exception.PackageNotFoundException;
import com.entropicbox.mockdi4j.model.DependencyMap;
import com.entropicbox.mockdi4j.model.DependencyTree;
import com.entropicbox.mockdi4j.util.ReflectionsUtils;

public class MockDIBuilder {
	private String basePackage;

	private Set<Class<?>> manuallyAddedClasses;
	private Set<Class<?>> manuallyExcludedClasses;

	protected MockDIBuilder() {}
	
	public MockDIBuilder(String basePackage) {
		this.basePackage = basePackage;
		manuallyAddedClasses = new HashSet<>();
		manuallyExcludedClasses = new HashSet<>();
	}

	public MockDI wire() {
		Set<Class<?>> classesToBeWired = ReflectionsUtils.getImplementationClassesFromBasePackage(basePackage);
		if (classesToBeWired.isEmpty()) {
			throw new PackageNotFoundException();
		}

		for (Class<?> manuallyExcludedClass : manuallyExcludedClasses) {
			classesToBeWired.remove(manuallyExcludedClass);
		}

		DependencyTree dependencyTree = new DependencyTree(classesToBeWired);

		for (Class<?> manuallyAddedClass : manuallyAddedClasses) {
			dependencyTree.add(manuallyAddedClass);
		}

		DependencyMap dependencyMap = new DependencyMap(dependencyTree);
		dependencyMap.wire();

		return new MockDI(dependencyMap);
	}

	public MockDIBuilder with(Class<?> clazz) {
		if (clazz == null)
			throw null;
		
		manuallyAddedClasses.add(clazz);
		return this;
	}

	public MockDIBuilder without(Class<?> clazz) {
		if (clazz == null)
			throw null;

		manuallyExcludedClasses.add(clazz);
		return this;
	}

	public MockDIBuilder replace(Class<?> old, Class<?> with) {
		if (old == null || with == null)
			throw null;
		
		manuallyAddedClasses.add(with);
		manuallyExcludedClasses.add(old);
		
		return this;
	}

}
