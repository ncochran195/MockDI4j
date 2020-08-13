package com.entropicbox.mockdi4j.model.test.simple_dependency_graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DependsOnSomeAbstractClass {
	@Getter
	private final SomeAbstractClass dep;
}
