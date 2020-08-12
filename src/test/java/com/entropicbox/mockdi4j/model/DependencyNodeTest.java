package com.entropicbox.mockdi4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.entropicbox.mockdi4j.model.DependencyTreeTest.DependsOnSomeAbstractClass;
import com.entropicbox.mockdi4j.model.DependencyTreeTest.ITestMockDao;

public class DependencyNodeTest {

	@Test
	public void testNewDependencyNode_null() {
		try {
			new DependencyNode(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testNewDependency_notNull() {
		DependencyNode node = new DependencyNode(ITestMockDao.class);
		assertNotNull(node);
		assertEquals(ITestMockDao.class, node.getDependencyClass());
	}

	@Test
	public void testAddChild_null() {
		DependencyNode parentNode = new DependencyNode(DependsOnSomeAbstractClass.class);
		try {
			parentNode.addChild(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testAddChild_toParent() {
		DependencyNode parentNode = new DependencyNode(DependsOnSomeAbstractClass.class);
		DependencyNode child = new DependencyNode(ITestMockDao.class);
		parentNode.addChild(child);

		assertEquals("The child should be under the parent", child.getDependencyClass(),
				parentNode.children().iterator().next().getDependencyClass());
	}

}
