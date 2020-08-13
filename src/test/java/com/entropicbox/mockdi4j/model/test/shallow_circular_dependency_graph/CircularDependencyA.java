package com.entropicbox.mockdi4j.model.test.shallow_circular_dependency_graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CircularDependencyA {
	@Getter
	private final CircularDependencyB dep;
}
