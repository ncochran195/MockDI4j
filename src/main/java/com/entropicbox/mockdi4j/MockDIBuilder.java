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

	private Set<String> manuallyAddedPackages;
	private Set<String> manuallyExcludedPackages;

	protected MockDIBuilder() {}
	
	public MockDIBuilder(String basePackage) {
		this.basePackage = basePackage;
		manuallyAddedClasses = new HashSet<>();
		manuallyExcludedClasses = new HashSet<>();
		
		manuallyAddedPackages = new HashSet<>();
		manuallyExcludedPackages = new HashSet<>();
	}

	public MockDI wire() {
		Set<Class<?>> classesToBeWired = getClassesToBeWired();
		
		DependencyMap wiredDependencyMap = getAndWireDependencyMap(classesToBeWired);
		
		return new MockDI(wiredDependencyMap);
	}

	private DependencyMap getAndWireDependencyMap(Set<Class<?>> classesToBeWired) {

		DependencyTree dependencyTree = new DependencyTree(classesToBeWired);

		DependencyMap dependencyMap = new DependencyMap(dependencyTree);
		dependencyMap.wire();
		
		return dependencyMap;
	}

	private Set<Class<?>> getClassesToBeWired() {
		Set<Class<?>> classesToBeWired = ReflectionsUtils.getImplementationClassesFromBasePackage(basePackage);
		if (classesToBeWired.isEmpty()) {
			throw new PackageNotFoundException();
		}

		for (String packageToBeAdded : manuallyAddedPackages)
		{
			Set<Class<?>> classesToBeAdded = 
					ReflectionsUtils.getImplementationClassesFromBasePackage(packageToBeAdded);
			classesToBeWired.addAll(classesToBeAdded);	
		}
		
		for (String packageToBeExcluded : manuallyExcludedPackages)
		{
			for (Class<?> classToBeWired : classesToBeWired)
			{
				if (classToBeWired.getPackageName().equalsIgnoreCase(packageToBeExcluded))
				{
					classesToBeWired.remove(classToBeWired);
				}
			}
		}
		
		for (Class<?> manuallyExcludedClass : manuallyExcludedClasses) {
			classesToBeWired.remove(manuallyExcludedClass);
		}
		
		for (Class<?> manuallyAddedClass : manuallyAddedClasses) {
			classesToBeWired.add(manuallyAddedClass);
		}

		return classesToBeWired;
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

	public MockDIBuilder with(String packageName) {
		if (packageName == null)
			throw null;

		manuallyAddedPackages.add(packageName);
		
		return this;
	}
	
	public MockDIBuilder without(String packageName) {
		if (packageName == null)
			throw null;

		manuallyExcludedPackages.add(packageName);
		
		return this;
	}

	public MockDIBuilder replace(String old, String with) {
		if (old == null || with == null)
			throw null;
		
		manuallyAddedPackages.add(with);
		manuallyExcludedPackages.add(old);
		
		return this;
	}

}
