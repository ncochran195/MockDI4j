package com.entropicbox.mockdi4j.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;

import com.entropicbox.mockdi4j.model.DependencyTreeTest.DependsOnSomeAbstractClass;
import com.entropicbox.mockdi4j.model.DependencyTreeTest.ITestMockDao;
import com.entropicbox.mockdi4j.model.DependencyTreeTest.SomeAbstractClassImpl;
import com.entropicbox.mockdi4j.model.DependencyTreeTest.TestMockDaoImpl;
import com.entropicbox.mockdi4j.model.DependencyTreeTest.TestMockService;

public class ReflectionsUtilsTest {

	@Test
	public void testGetClassesFromBasePackage_null() {
		try {
			ReflectionsUtils.getClassesFromBasePackage(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testGetClassesFromBasePackage_doesNotExist() {
		Set<Class<?>> classes = ReflectionsUtils.getClassesFromBasePackage("com.doesnotexist");

		assertTrue(classes.isEmpty());
	}

	@Test
	public void testGetClassesFromBasePackage_exists() {
		Set<Class<?>> classes = ReflectionsUtils.getClassesFromBasePackage("com.entropicbox.mockdi4j");

		assertTrue("Expecting at least this test class and the class under test", classes.size() >= 2);
	}

	@Test
	public void testClassDependsOn_nulls() {
		try {
			ReflectionsUtils.classDependsOn(null, null);
			fail();
		} catch (NullPointerException e) {
		}

		try {
			ReflectionsUtils.classDependsOn(DependsOnSomeAbstractClass.class, null);
			fail();
		} catch (NullPointerException e) {
		}

		try {
			ReflectionsUtils.classDependsOn(null, ITestMockDao.class);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testClassDependsOn_parentToChild() {
		assertTrue("TestMockService has ITestMockDao as a required constructor arg",
				ReflectionsUtils.classDependsOn(TestMockService.class, ITestMockDao.class));
	}

	@Test
	public void testClassDependsOn_childToParent() {
		assertFalse("ITestMockDao has no constructor args",
				ReflectionsUtils.classDependsOn(ITestMockDao.class, DependsOnSomeAbstractClass.class));
	}

	@Test
	public void testClassDependsOn_childSubclassToParent() {
		assertTrue(
				"SomeAbstractClassImpl 'is a' SomeAbstractClass, so it should also satisfy DependsOnSomeAbstractClass",
				ReflectionsUtils.classDependsOn(DependsOnSomeAbstractClass.class, SomeAbstractClassImpl.class));
	}

	@Test
	public void testClassDependsOn_interfaceToParent() {
		assertTrue("DependsOnLeafDependencyA has LeafDependencyA as a required constructor arg",
				ReflectionsUtils.classDependsOn(TestMockService.class, ITestMockDao.class));
	}

	@Test
	public void testClassDependsOn_interfaceSubtypeToParent() {
		assertTrue("TestMockDaoImpl 'is a' ITestMockDao, so it should also satisfy TestMockService",
				ReflectionsUtils.classDependsOn(TestMockService.class, TestMockDaoImpl.class));
	}

}
