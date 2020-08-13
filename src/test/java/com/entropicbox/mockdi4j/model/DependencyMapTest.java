package com.entropicbox.mockdi4j.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.entropicbox.mockdi4j.exception.DependencyNotFoundException;
import com.entropicbox.mockdi4j.exception.WireDependencyException;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.DependsOnSomeAbstractClass;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.SomeAbstractClass;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.SomeAbstractClassImpl;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.ITestMockDao;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockDaoImpl;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.LeafA;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.LeafB;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.MiddleA;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.MiddleB;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.RootAB;

public class DependencyMapTest {

	@Test
	public void testNewDependencyMap_null() {
		try {
			new DependencyMap(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testNewDependencyMap_emptyTree() {
		DependencyMap emptyMap = new DependencyMap(new DependencyTree());

		assertNotNull(emptyMap);
	}

	@Test
	public void testNewDependencyMap_nonEmptyTree() {
		DependencyTree singleDTree = new DependencyTree();
		singleDTree.add(SomeAbstractClass.class);

		DependencyMap singleDMap = new DependencyMap(singleDTree);

		assertNotNull(singleDMap);
	}

	@Test
	public void testWire_emptyMap() {
		DependencyMap emptyMap = new DependencyMap(new DependencyTree());
		emptyMap.wire();

		assertNotNull(emptyMap);
	}

	@Test
	public void testWire_singleDependency() {
		DependencyTree singleDTree = new DependencyTree();
		singleDTree.add(SomeAbstractClassImpl.class);

		DependencyMap singleDMap = new DependencyMap(singleDTree);
		singleDMap.wire();

		assertNotNull(singleDMap);
	}

	@Test
	public void testWire_unsatisfiedDependency() {
		DependencyTree unsatisfiedDTree = new DependencyTree();
		unsatisfiedDTree.add(DependsOnSomeAbstractClass.class);

		DependencyMap unsatisfiedDMap = new DependencyMap(unsatisfiedDTree);

		try {
			unsatisfiedDMap.wire();
			fail();
		} catch (WireDependencyException e) {
		}
	}

	@Test
	public void testGet_notFound() {
		DependencyMap emptyMap = new DependencyMap(new DependencyTree());
		emptyMap.wire();
		try {
			emptyMap.get(SomeAbstractClassImpl.class);
			fail();
		} catch (DependencyNotFoundException e) {
		}
	}

	@Test
	public void testGet_singleDependency() {
		DependencyTree singleDTree = new DependencyTree();
		singleDTree.add(SomeAbstractClassImpl.class);

		DependencyMap singleDMap = new DependencyMap(singleDTree);
		singleDMap.wire();

		SomeAbstractClassImpl instance = singleDMap.get(SomeAbstractClassImpl.class);

		assertNotNull(instance);
	}

	@Test
	public void testGet_singleDependency_abstractClass() {
		DependencyTree singleDTree = new DependencyTree();
		singleDTree.add(SomeAbstractClassImpl.class);

		DependencyMap singleDMap = new DependencyMap(singleDTree);
		singleDMap.wire();

		SomeAbstractClass instance = singleDMap.get(SomeAbstractClass.class);

		assertNotNull(instance);
	}
	
	@Test
	public void testGet_siblingNodes()
	{
		DependencyTree siblingDTree = new DependencyTree();
		siblingDTree.add(SomeAbstractClassImpl.class);
		siblingDTree.add(TestMockDaoImpl.class);

		DependencyMap siblingDMap = new DependencyMap(siblingDTree);
		siblingDMap.wire();

		SomeAbstractClass sibling1 = siblingDMap.get(SomeAbstractClass.class);
		ITestMockDao sibling2 = siblingDMap.get(ITestMockDao.class);

		assertNotNull(sibling1);
		assertNotNull(sibling2);
	}

	@Test
	public void testGet_parentChild() {
		DependencyTree parentChildDTree = new DependencyTree();
		parentChildDTree.add(SomeAbstractClassImpl.class);
		parentChildDTree.add(DependsOnSomeAbstractClass.class);

		DependencyMap parentChildDMap = new DependencyMap(parentChildDTree);
		parentChildDMap.wire();

		SomeAbstractClass child = parentChildDMap.get(SomeAbstractClass.class);
		DependsOnSomeAbstractClass parent = parentChildDMap.get(DependsOnSomeAbstractClass.class);

		assertNotNull(child);
		assertNotNull(parent);
	}
	
	
	@Test
	public void testGet_triangularDependencies()
	{
		DependencyTree triangularDTree = new DependencyTree();
		triangularDTree.add(LeafA.class);
		triangularDTree.add(LeafB.class);
		triangularDTree.add(MiddleA.class);
		triangularDTree.add(MiddleB.class);
		triangularDTree.add(RootAB.class);

		DependencyMap triangleDMap = new DependencyMap(triangularDTree);
		triangleDMap.wire();

		assertNotNull(triangleDMap.get(LeafA.class));
		assertNotNull(triangleDMap.get(LeafB.class));
		assertNotNull(triangleDMap.get(MiddleA.class));
		assertNotNull(triangleDMap.get(MiddleB.class));
		assertNotNull(triangleDMap.get(RootAB.class));
	}
	
}
