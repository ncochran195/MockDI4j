package com.entropicbox.mockdi4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.entropicbox.mockdi4j.exception.CircularDependencyException;
import com.entropicbox.mockdi4j.exception.DependencyNotFoundException;
import com.entropicbox.mockdi4j.exception.PackageNotFoundException;
import com.entropicbox.mockdi4j.exception.UnsatisfiedDependencyException;
import com.entropicbox.mockdi4j.model.test.independant_dependency_graph.IndependentClassA;
import com.entropicbox.mockdi4j.model.test.independant_dependency_graph.IndependentClassB;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.DependsOnSomeAbstractClass;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.SomeAbstractClass;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.ITestMockDao;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockController;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockService;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.LeafA;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.LeafB;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.MiddleA;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.MiddleB;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.RootAB;

public class MockDiTest {

	@Test
	public void testMockDi_ofNull() {
		try {
			MockDI.of(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testMockDi_ofNonExistantPackage() {
		MockDIBuilder builder = MockDI.of("com.doesNotExist");
		assertNotNull(builder);
	}

	@Test
	public void testMockDi_ofExistantPackage() {
		MockDIBuilder builder = MockDI.of("com.entropicbox.mockdi4j");
		assertNotNull(builder);
	}

	@Test
	public void testWire_nonExistentPackage() {
		try {
			MockDI.of("com.doesNotExist").wire();
			fail();
		} catch (PackageNotFoundException e) {
		}
	}

	@Test
	public void testGet_simpleDependencyGraph() {
		MockDI mock = MockDI.of("com.entropicbox.mockdi4j.model.test.simple_dependency_graph").wire();

		SomeAbstractClass someAbstractClass = mock.get(SomeAbstractClass.class);
		DependsOnSomeAbstractClass dependsOnSomeAbstractClass = mock.get(DependsOnSomeAbstractClass.class);

		assertNotNull(someAbstractClass);
		assertNotNull(dependsOnSomeAbstractClass);

		assertEquals(someAbstractClass, dependsOnSomeAbstractClass.getDep());
	}

	@Test
	public void testGet_independentDependencyGraph() {
		MockDI mock = MockDI.of("com.entropicbox.mockdi4j.model.test.independant_dependency_graph").wire();
		IndependentClassA a = mock.get(IndependentClassA.class);
		IndependentClassB b = mock.get(IndependentClassB.class);

		assertNotNull(a);
		assertNotNull(b);
	}

	@Test
	public void testGet_triangularDependencyGraph() {
		MockDI mock = MockDI.of("com.entropicbox.mockdi4j.model.test.triangular_dependency_graph").wire();

		LeafA leafA = mock.get(LeafA.class);
		LeafB leafB = mock.get(LeafB.class);
		MiddleA middleA = mock.get(MiddleA.class);
		MiddleB middleB = mock.get(MiddleB.class);
		RootAB rootAB = mock.get(RootAB.class);

		assertNotNull(leafA);
		assertNotNull(leafB);
		assertNotNull(middleA);
		assertNotNull(middleB);
		assertNotNull(rootAB);
	}

	@Test
	public void testGet_threeLevelDependencyGraph() {
		MockDI mock = MockDI.of("com.entropicbox.mockdi4j.model.test.three_level_dependency_graph").wire();

		ITestMockDao testMockDao = mock.get(ITestMockDao.class);
		TestMockService testMockService = mock.get(TestMockService.class);
		TestMockController testMockController = mock.get(TestMockController.class);

		assertNotNull(testMockDao);
		assertNotNull(testMockService);
		assertNotNull(testMockController);
	}

	@Test
	public void testGet_unsatisfiedDependencyGraph() {

		MockDIBuilder builder = MockDI.of("com.entropicbox.mockdi4j.model.test.unsatisfied_dependency_graph");

		try {
			builder.wire();
			fail();
		} catch (UnsatisfiedDependencyException e) {
		}
	}

	@Test
	public void testGet_shallowCircularDependencyGraph() {

		MockDIBuilder builder = MockDI.of("com.entropicbox.mockdi4j.model.test.shallow_circular_dependency_graph");

		try {
			builder.wire();
			fail();
		} catch (CircularDependencyException e) {
		}
	}

	@Test
	public void testGet_deepCircularDependencyGraph() {

		MockDIBuilder builder = MockDI.of("com.entropicbox.mockdi4j.model.test.deep_circular_dependency_graph");

		try {
			builder.wire();
			fail();
		} catch (CircularDependencyException e) {
		}
	}

	@Test
	public void testGet_dependencyNotFound() {
		MockDI mock = MockDI.of("com.entropicbox.mockdi4j.model.test.independant_dependency_graph").wire();

		try {
			mock.get(ITestMockDao.class);
			fail("Test mock dao should not be loaded when the independant_dependency_graph is loaded");
		} catch (DependencyNotFoundException e) {

		}
	}

}
