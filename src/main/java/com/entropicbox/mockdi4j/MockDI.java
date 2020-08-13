package com.entropicbox.mockdi4j;

import com.entropicbox.mockdi4j.model.DependencyMap;

public class MockDI {
	private final DependencyMap dependencyMap;
	
	public MockDI(DependencyMap dependencyMap) {
		this.dependencyMap = dependencyMap;
	}

	public static MockDIBuilder of(String basePackage) {
		if (basePackage == null)
			throw null;
				
		return new MockDIBuilder(basePackage);
	}

	public <T> T get(Class<T> clazz) {
		return this.dependencyMap.get(clazz);
	}

}
