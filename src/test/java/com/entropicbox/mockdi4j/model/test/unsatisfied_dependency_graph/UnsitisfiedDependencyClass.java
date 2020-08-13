package com.entropicbox.mockdi4j.model.test.unsatisfied_dependency_graph;

import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockDaoImpl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnsitisfiedDependencyClass {
	@Getter
	private final TestMockDaoImpl dep;
}
