package com.entropicbox.mockdi4j.model.test.triangular_dependency_graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RootAB {
	@Getter
	private final MiddleA depA;
	@Getter
	private final MiddleB depB;
}
