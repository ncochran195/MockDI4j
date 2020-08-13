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
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockDaoImpl;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockService;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.LeafA;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.LeafB;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.MiddleA;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.MiddleB;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.RootAB;
import com.entropicbox.mockdi4j.model.test.unsatisfied_dependency_graph.UnsatisfiedDependencyClass;

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

	@Test
	public void testWith_null() {
		try {
			MockDI.of("com.entropicbox.mockdi4j.model.test.unsatisfied_dependency_graph").with(null).wire();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testWith_class() {
		MockDI mock = MockDI.of("com.entropicbox.mockdi4j.model.test.unsatisfied_dependency_graph")
				.with(TestMockDaoImpl.class).wire();

		UnsatisfiedDependencyClass newlySatisfiedDependency = mock.get(UnsatisfiedDependencyClass.class);
		assertNotNull(newlySatisfiedDependency);
	}

	@Test
	public void testWithout_null() {
		try {
			MockDI.of("com.entropicbox.mockdi4j.model.test.unsatisfied_dependency_graph").without(null).wire();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testWithout_class() {
		MockDI mock = MockDI.of("com.entropicbox.mockdi4j.model.test.triangular_dependency_graph")
				.without(MiddleA.class).without(LeafA.class).without(RootAB.class).wire();

		LeafB leafB = mock.get(LeafB.class);
		MiddleB middleB = mock.get(MiddleB.class);

		assertNotNull(leafB);
		assertNotNull(middleB);

		try {
			mock.get(MiddleA.class);
			fail();
		} catch (DependencyNotFoundException e) {

		}

		try {
			mock.get(LeafA.class);
			fail();
		} catch (DependencyNotFoundException e) {

		}

		try {
			mock.get(RootAB.class);
			fail();
		} catch (DependencyNotFoundException e) {

		}
	}

	@Test
	public void testReplace_nulls() {
		try {
			MockDI.of("com.entropicbox.mockdi4j.model.test.unsatisfied_dependency_graph").replace(null, LeafA.class)
					.wire();
		} catch (NullPointerException e) {
		}

		try {
			MockDI.of("com.entropicbox.mockdi4j.model.test.unsatisfied_dependency_graph").replace(LeafA.class, null)
					.wire();
		} catch (NullPointerException e) {
		}

	}

	@Test
	public void testReplace_classes() {
		MockDI mock = 
				MockDI.of("com.entropicbox.mockdi4j.model.test.simple_dependency_graph").
				replace(DependsOnSomeAbstractClass.class, LeafA.class).wire();

		try {
			mock.get(DependsOnSomeAbstractClass.class);
			fail();
		} catch (DependencyNotFoundException e) {

		}

		assertNotNull(mock.get(LeafA.class));
		assertNotNull(mock.get(SomeAbstractClass.class));
	}

}
