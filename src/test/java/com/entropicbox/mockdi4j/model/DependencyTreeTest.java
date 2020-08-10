package com.entropicbox.mockdi4j.model;

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
	public void testNewDependencyTree()
	{
		DependencyTree emptyDTree = new DependencyTree();
		assertNotNull(emptyDTree);
		assertTrue(emptyDTree.size() == 0);
	}
	
	@Test
	public void testNewDependencyTree_null()
	{
		try
		{
			new DependencyTree(null);
			fail();
		}
		catch(NullPointerException e)
		{}
	}
	
	@Test
	public void testNewDependencyTree_emptySet()
	{
		DependencyTree emptyDTree = new DependencyTree(new HashSet<Class<?>>());
		assertNotNull(emptyDTree);
		assertTrue(emptyDTree.size() == 0);
	}
	
	@Test
	public void testNewDependencyTree_oneElement()
	{
		Set<Class<?>> singleDependencySet = new HashSet<>();
		singleDependencySet.add(LeafDependencyA.class);
		
		DependencyTree singleDTree = new DependencyTree(singleDependencySet);
		assertNotNull(singleDTree);
		assertTrue("There should be one element in the tree", singleDTree.size() == 1);
		assertTrue("There should only be one root", singleDTree.roots().size() == 1);
		assertTrue("The one root's node should match the single dependency", 
				singleDTree.roots().iterator().next().
					getDependencyClass() == LeafDependencyA.class);
	}
	
	@Test
	public void testAdd_null()
	{
		try
		{
			new DependencyTree().add(null);
			fail();
		}
		catch(NullPointerException e)
		{}
	}
	
	@Test
	public void testAdd_oneElement()
	{
		DependencyTree singleDTree = new DependencyTree();
		singleDTree.add(LeafDependencyA.class);
		assertNotNull(singleDTree);
		assertTrue("There should be one element in the tree", singleDTree.size() == 1);
		assertTrue("There should only be one root", singleDTree.roots().size() == 1);
		assertTrue("The one root's node should match the single dependency", 
				singleDTree.roots().iterator().next().
					getDependencyClass() == LeafDependencyA.class);
	}

	@Test
	public void testAdd_siblingElements()
	{
		DependencyTree dualSiblingDTree = new DependencyTree();
		dualSiblingDTree.add(LeafDependencyA.class);
		dualSiblingDTree.add(LeafDependencyB.class);
		assertNotNull(dualSiblingDTree);
		assertTrue("There should be two elements in the tree", dualSiblingDTree.size() == 2);
		assertTrue("There should 2 roots", dualSiblingDTree.roots().size() == 2);
	}

	/*@Test
	public void testAdd_parentChildElements()
	{
		DependencyTree parentChildDTree = new DependencyTree();
		parentChildDTree.add(LeafDependencyA.class);
		parentChildDTree.add(DependsOnLeafDependencyA.class);
		assertNotNull(parentChildDTree);
		assertTrue("There should be two elements in the tree", parentChildDTree.size() == 2);
		assertTrue("There should only be one root", parentChildDTree.roots().size() == 1);
		assertTrue("The one root's node should match the parent dependency", 
				parentChildDTree.roots().iterator().next().
					getDependencyClass() == DependsOnLeafDependencyA.class);
	}*/

	
	class LeafDependencyA {
	}
	class LeafDependencyB {
	}
	
	@RequiredArgsConstructor
	class DependsOnLeafDependencyA
	{
		@Getter
		private final LeafDependencyA dep;
	}


}
