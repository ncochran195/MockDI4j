package com.entropicbox.mockdi4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
		singleDependencySet.add(SomeAbstractClass.class);

		DependencyTree singleDTree = new DependencyTree(singleDependencySet);

		assertNotNull(singleDTree);

		assertEquals("There should be one element in the tree", 1, singleDTree.size());

		assertEquals("There should only be one root", 1, singleDTree.roots().size());

		assertEquals("The one root's node should match the single dependency", SomeAbstractClass.class,
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
	public void testAdd_oneElement() {
		DependencyTree singleDTree = new DependencyTree();
		singleDTree.add(SomeAbstractClass.class);

		assertEquals("There should be one element in the tree", 1, singleDTree.size());

		assertEquals("There should only be one root", 1, singleDTree.roots().size());

		assertEquals("The one root's node should match the single dependency", SomeAbstractClass.class,
				singleDTree.roots().iterator().next().getDependencyClass());
	}

	@Test
	public void testAdd_siblingElements() {
		DependencyTree dualSiblingDTree = new DependencyTree();
		dualSiblingDTree.add(SomeAbstractClass.class);
		dualSiblingDTree.add(SomeAbstractClassImpl.class);

		assertEquals("There should be two elements in the tree", 2, dualSiblingDTree.size());

		assertEquals("There should 2 roots", 2, dualSiblingDTree.roots().size());
	}

	@Test
	public void testAdd_parentChildElements() {
		DependencyTree parentChildDTree = new DependencyTree();
		parentChildDTree.add(SomeAbstractClass.class);
		parentChildDTree.add(DependsOnSomeAbstractClass.class);

		assertEquals("There should be two elements in the tree", 2, parentChildDTree.size());

		assertEquals("There should only be one root", 1, parentChildDTree.roots().size());

		assertEquals("The one root's node should match the parent's dependency", DependsOnSomeAbstractClass.class,
				parentChildDTree.roots().iterator().next().getDependencyClass());

		assertEquals("There should only be one child of the root", 1,
				parentChildDTree.roots().iterator().next().children().size());

		assertEquals("The one root's child should be the other class", SomeAbstractClass.class,
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
	public void testAdd_circularDependency() {
		DependencyTree circularDTree = new DependencyTree();
		circularDTree.add(CircularDependencyA.class);
		circularDTree.add(CircularDependencyB.class);

		assertEquals("There should be two elements in the tree", 2, circularDTree.size());

		assertEquals("There should be one root", 1, circularDTree.roots().size());
	}

	// ---------------------------- //
	// Simple Test Dependency Graph //
	// ---------------------------- //
	public abstract class SomeAbstractClass {
	}

	public class SomeAbstractClassImpl extends SomeAbstractClass {
	}

	@RequiredArgsConstructor
	public class DependsOnSomeAbstractClass {
		@Getter
		private final SomeAbstractClass dep;
	}

	// ---------------------------- //
	// Three Level Dependency Graph //
	// ---------------------------- //
	public interface ITestMockDao {
	}

	public class TestMockDaoImpl implements ITestMockDao {
	}

	@RequiredArgsConstructor
	public class TestMockService {
		@Getter
		private final ITestMockDao dep;
	}

	@RequiredArgsConstructor
	public class TestMockController {
		@Getter
		private final TestMockService dep;
	}

	// --------------------------- //
	// Triangular Dependency Graph //
	// --------------------------- //
	public class LeafA {
	}

	public class LeafB {
	}

	@RequiredArgsConstructor
	public class MiddleA {
		@Getter
		private final LeafA dep;
	}

	@RequiredArgsConstructor
	public class MiddleB {
		@Getter
		private final LeafB dep;
	}

	@RequiredArgsConstructor
	public class RootAB {
		@Getter
		private final MiddleA depA;
		@Getter
		private final MiddleB depB;
	}

	// ------------------------- //
	// Circular Dependency Graph //
	// ------------------------- //
	@RequiredArgsConstructor
	public class CircularDependencyA {
		@Getter
		private final CircularDependencyB dep;
	}

	@RequiredArgsConstructor
	public class CircularDependencyB {
		@Getter
		private final CircularDependencyA dep;
	}

}
