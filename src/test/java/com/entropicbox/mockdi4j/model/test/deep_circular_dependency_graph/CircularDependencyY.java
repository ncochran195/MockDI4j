package com.entropicbox.mockdi4j.model.test.deep_circular_dependency_graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CircularDependencyY {
	
	@Getter
	private final CircularDependencyZ dep;
}
