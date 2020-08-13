package com.entropicbox.mockdi4j.model.test.three_level_dependency_graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestMockService {
	@Getter
	private final ITestMockDao dep;
}
