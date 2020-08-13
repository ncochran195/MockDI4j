package com.entropicbox.mockdi4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.entropicbox.mockdi4j.exception.CircularDependencyException;
import com.entropicbox.mockdi4j.exception.DuplicateDependencyException;
import com.entropicbox.mockdi4j.model.test.deep_circular_dependency_graph.CircularDependencyX;
import com.entropicbox.mockdi4j.model.test.deep_circular_dependency_graph.CircularDependencyY;
import com.entropicbox.mockdi4j.model.test.deep_circular_dependency_graph.CircularDependencyZ;
import com.entropicbox.mockdi4j.model.test.shallow_circular_dependency_graph.CircularDependencyA;
import com.entropicbox.mockdi4j.model.test.shallow_circular_dependency_graph.CircularDependencyB;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.DependsOnSomeAbstractClass;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.SomeAbstractClassImpl;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.ITestMockDao;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockController;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockDaoImpl;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockService;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.LeafA;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.LeafB;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.MiddleA;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.MiddleB;
import com.entropicbox.mockdi4j.model.test.triangular_dependency_graph.RootAB;

public class DependencyTreeTest {

	@Test
	public void testNewDependencyTree() {
		DependencyTree emptyDTree = new DependencyTree();

		assertNotNull(emptyDTree);

		assertTrue(emptyDTree.size() == 0);
	}

	@Test
	public void testNewDependencyTree_null() {
		try {
			new DependencyTree(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testNewDependencyTree_emptySet() {
		DependencyTree emptyDTree = new DependencyTree(new HashSet<Class<?>>());

		assertNotNull(emptyDTree);

		assertEquals(0, emptyDTree.size());
	}

	@Test
	public void testNewDependencyTree_oneElement() {
		Set<Class<?>> singleDependencySet = new HashSet<>();
		singleDependencySet.add(SomeAbstractClassImpl.class);

		DependencyTree singleDTree = new DependencyTree(singleDependencySet);

		assertNotNull(singleDTree);

		assertEquals("There should be one element in the tree", 1, singleDTree.size());

		assertEquals("There should only be one root", 1, singleDTree.roots().size());

		assertEquals("The one root's node should match the single dependency", 
				SomeAbstractClassImpl.class,
				singleDTree.roots().iterator().next().getDependencyClass());
	}

	@Test
	public void testAdd_null() {
		try {
			new DependencyTree().add(null);
			fail();
		} catch (NullPointerException e) {
		}
	}
	
	@Test
	public void testAdd_duplicateDependencies() {
		DependencyTree duplicateDTree = new DependencyTree();
		duplicateDTree.add(SomeAbstractClassImpl.class);
		try {
			duplicateDTree.add(SomeAbstractClassImpl.class);
			fail();
		} catch (DuplicateDependencyException e) {
		}
	}

	@Test
	public void testAdd_interface() {
		DependencyTree singleDTree = new DependencyTree();
		singleDTree.add(ITestMockDao.class);

		assertEquals("The element should have not been added to the tree", 
				0, singleDTree.size());
	}

	@Test
	public void testAdd_oneElement() {
		DependencyTree singleDTree = new DependencyTree();
		singleDTree.add(SomeAbstractClassImpl.class);

		assertEquals("There should be one element in the tree", 1, singleDTree.size());

		assertEquals("There should only be one root", 1, singleDTree.roots().size());

		assertEquals("The one root's node should match the single dependency", 
				SomeAbstractClassImpl.class,
				singleDTree.roots().iterator().next().getDependencyClass());
	}

	@Test
	public void testAdd_siblingElements() {
		DependencyTree dualSiblingDTree = new DependencyTree();
		dualSiblingDTree.add(TestMockDaoImpl.class);
		dualSiblingDTree.add(SomeAbstractClassImpl.class);

		assertEquals("There should be two elements in the tree", 2, dualSiblingDTree.size());

		assertEquals("There should 2 roots", 2, dualSiblingDTree.roots().size());
	}

	@Test
	public void testAdd_parentChildElements() {
		DependencyTree parentChildDTree = new DependencyTree();
		parentChildDTree.add(SomeAbstractClassImpl.class);
		parentChildDTree.add(DependsOnSomeAbstractClass.class);

		assertEquals("There should be two elements in the tree", 2, parentChildDTree.size());

		assertEquals("There should only be one root", 1, parentChildDTree.roots().size());

		assertEquals("The one root's node should match the parent's dependency", DependsOnSomeAbstractClass.class,
				parentChildDTree.roots().iterator().next().getDependencyClass());

		assertEquals("There should only be one child of the root", 1,
				parentChildDTree.roots().iterator().next().children().size());

		assertEquals("The one root's child should be the other class", SomeAbstractClassImpl.class,
				parentChildDTree.roots().iterator().next().children().iterator().next().getDependencyClass());
	}

	@Test
	public void testAdd_threeLevels() {
		DependencyTree threeLevelDTree = new DependencyTree();
		threeLevelDTree.add(TestMockDaoImpl.class);
		threeLevelDTree.add(TestMockService.class);
		threeLevelDTree.add(TestMockController.class);

		assertEquals("There should be three elements in the tree", 3, threeLevelDTree.size());

		assertEquals("There should only be one root", 1, threeLevelDTree.roots().size());

		assertEquals("The one root's node should match the parent's dependency", TestMockController.class,
				threeLevelDTree.roots().iterator().next().getDependencyClass());

		assertEquals("There should only be one child of the controller", 1,
				threeLevelDTree.roots().iterator().next().children().size());

		assertEquals("That node's only child should be the service", TestMockService.class,
				threeLevelDTree.roots().iterator().next().children().iterator().next().getDependencyClass());

		assertEquals("There should only be one child of the service", 1,
				threeLevelDTree.roots().iterator().next().children().iterator().next().children().size());

		assertEquals("That node's only child should be the service", TestMockDaoImpl.class, threeLevelDTree.roots()
				.iterator().next().children().iterator().next().children().iterator().next().getDependencyClass());

	}

	@Test
	public void testAdd_threeLevels_reverseOrder() {
		DependencyTree threeLevelDTree = new DependencyTree();
		threeLevelDTree.add(TestMockController.class);
		threeLevelDTree.add(TestMockService.class);
		threeLevelDTree.add(TestMockDaoImpl.class);

		assertEquals("There should be three elements in the tree", 3, threeLevelDTree.size());

		assertEquals("There should only be one root", 1, threeLevelDTree.roots().size());

		assertEquals("The one root's node should match the parent's dependency", TestMockController.class,
				threeLevelDTree.roots().iterator().next().getDependencyClass());

		assertEquals("There should only be one child of the controller", 1,
				threeLevelDTree.roots().iterator().next().children().size());

		assertEquals("That node's only child should be the service", TestMockService.class,
				threeLevelDTree.roots().iterator().next().children().iterator().next().getDependencyClass());

		assertEquals("There should only be one child of the service", 1,
				threeLevelDTree.roots().iterator().next().children().iterator().next().children().size());

		assertEquals("That node's only child should be the service", TestMockDaoImpl.class, threeLevelDTree.roots()
				.iterator().next().children().iterator().next().children().iterator().next().getDependencyClass());
	}

	@Test
	public void testAdd_mergeTwoTrees() {
		DependencyTree threeLevelDTree = new DependencyTree();
		threeLevelDTree.add(LeafA.class);
		threeLevelDTree.add(MiddleA.class);
		threeLevelDTree.add(LeafB.class);
		threeLevelDTree.add(MiddleB.class);

		assertEquals("There should be four elements in the tree", 4, threeLevelDTree.size());

		assertEquals("There should be two roots", 2, threeLevelDTree.roots().size());

		// Add the common root to both existing roots
		threeLevelDTree.add(RootAB.class);

		assertEquals("There should be five elements in the tree", 5, threeLevelDTree.size());

		assertEquals("There should be one root", 1, threeLevelDTree.roots().size());
	}

	@Test
	public void testAdd_unsatisfiedDependency() {
		DependencyTree unsatisfiedDTree = new DependencyTree();
		unsatisfiedDTree.add(DependsOnSomeAbstractClass.class);

		assertEquals("There should be one element in the tree", 1, unsatisfiedDTree.size());

		assertEquals("There should be one root", 1, unsatisfiedDTree.roots().size());
	}

	@Test
	public void testAdd_shallowCircularDependency() {
		DependencyTree circularDTree = new DependencyTree();
		circularDTree.add(CircularDependencyA.class);
		try
		{
			circularDTree.add(CircularDependencyB.class);
			fail();
		}
		catch (CircularDependencyException e)
		{}
		
		assertTrue("Tree should be cleared after CircularDependencyException in order to avoid referential issues", 
				circularDTree.roots().isEmpty());
	}



	@Test
	public void testAdd_deepCircularDependency() {
		DependencyTree circularDTree = new DependencyTree();
		circularDTree.add(CircularDependencyX.class);
		circularDTree.add(CircularDependencyY.class);
		try
		{
			circularDTree.add(CircularDependencyZ.class);
			fail();
		}
		catch (CircularDependencyException e)
		{}
		
		assertTrue("Tree should be cleared after CircularDependencyException in order to avoid referential issues", 
				circularDTree.roots().isEmpty());
	}

}
