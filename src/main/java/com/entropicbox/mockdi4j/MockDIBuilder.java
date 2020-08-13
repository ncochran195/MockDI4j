package com.entropicbox.mockdi4j;

import java.util.Set;

import com.entropicbox.mockdi4j.exception.PackageNotFoundException;
import com.entropicbox.mockdi4j.model.DependencyMap;
import com.entropicbox.mockdi4j.model.DependencyTree;
import com.entropicbox.mockdi4j.util.ReflectionsUtils;

public class MockDIBuilder {
	private String basePackage;
	
	public MockDIBuilder(String basePackage) {
		this.basePackage = basePackage;
	}

	public MockDI wire() {
		Set<Class<?>> classesToBeWired = 
				ReflectionsUtils.getImplementationClassesFromBasePackage(basePackage);
		if (classesToBeWired.isEmpty())
		{
			throw new PackageNotFoundException();
		}
		DependencyTree dependencyTree = new DependencyTree(classesToBeWired);
		DependencyMap dependencyMap = new DependencyMap(dependencyTree);
		dependencyMap.wire();
		
		return new MockDI(dependencyMap);
	}

}
