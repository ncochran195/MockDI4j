package com.entropicbox.mockdi4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.entropicbox.mockdi4j.model.DependencyTreeTest.DependsOnLeafDependencyA;
import com.entropicbox.mockdi4j.model.DependencyTreeTest.LeafDependencyA;

public class DependencyNodeTest {

	@Test
	public void testNewDependencyNode_null()
	{
		try
		{
			new DependencyNode(null);
			fail();
		}
		catch(NullPointerException e)
		{}
	}
	
	@Test
	public void testNewDependency_notNull()
	{
		DependencyNode node = new DependencyNode(LeafDependencyA.class);
		assertNotNull(node);
		assertEquals(LeafDependencyA.class, node.getDependencyClass());
	}
	
	@Test
	public void testAddChild_null()
	{
		DependencyNode parentNode = new DependencyNode(DependsOnLeafDependencyA.class);
		try
		{
			parentNode.addChild(null);
			fail();
		}
		catch (NullPointerException e)
		{}
	}
	
	@Test
	public void testAddChild_toParent()
	{
		DependencyNode parentNode = new DependencyNode(DependsOnLeafDependencyA.class);
		DependencyNode child = new DependencyNode(LeafDependencyA.class);
		parentNode.addChild(child);
		
		assertEquals(
				"The child should be under the parent",
				child.getDependencyClass(), 
				parentNode.children().iterator().next().getDependencyClass());
	}
	
	
}
