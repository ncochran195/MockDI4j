package com.entropicbox.mockdi4j.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.util.Set;

import org.junit.Test;

import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.DependsOnSomeAbstractClass;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.SomeAbstractClass;
import com.entropicbox.mockdi4j.model.test.simple_dependency_graph.SomeAbstractClassImpl;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.ITestMockDao;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockDaoImpl;
import com.entropicbox.mockdi4j.model.test.three_level_dependency_graph.TestMockService;

public class ReflectionsUtilsTest {

	@Test
	public void testGetClassesFromBasePackage_null() {
		try {
			ReflectionsUtils.getImplementationClassesFromBasePackage(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testGetClassesFromBasePackage_doesNotExist() {
		Set<Class<?>> classes = ReflectionsUtils.getImplementationClassesFromBasePackage("com.doesnotexist");

		assertTrue(classes.isEmpty());
	}

	@Test
	public void testGetClassesFromBasePackage_exists() {
		Set<Class<?>> classes = ReflectionsUtils.getImplementationClassesFromBasePackage("com.entropicbox.mockdi4j");

		assertTrue("Expecting at least this test class and the class under test", classes.size() >= 2);
	}

	@Test
	public void testGetClassesFromBasePackage_ignoreAbstract() {
		Set<Class<?>> classes = ReflectionsUtils
				.getImplementationClassesFromBasePackage("com.entropicbox.mockdi4j.model.test.simple_dependency_graph");

		assertEquals("The abstract class should be ignored", 2, classes.size());
		assertTrue(classes.contains(SomeAbstractClassImpl.class));
		assertTrue(classes.contains(DependsOnSomeAbstractClass.class));
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

	@Test
	public void testNewLeafInstance_null() {
		try {
			ReflectionsUtils.newLeafInstance(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testNewLeafInstance_notNull() {
		SomeAbstractClassImpl instance = ReflectionsUtils.newLeafInstance(SomeAbstractClassImpl.class);
		assertNotNull(instance);
	}

	@Test
	public void testGetDependentsOf_null() {
		try {
			ReflectionsUtils.getDependentsOf(null);
			fail();
		} catch (NullPointerException e) {

		}
	}

	@Test
	public void testGetDependentsOf_leafNode() {
		Class<?>[] dependencies = ReflectionsUtils.getDependentsOf(SomeAbstractClassImpl.class);
		assertEquals(0, dependencies.length);
	}

	@Test
	public void testGetDependentsOf_parentNode() {
		Class<?>[] dependencies = ReflectionsUtils.getDependentsOf(DependsOnSomeAbstractClass.class);
		assertEquals(1, dependencies.length);
		assertEquals(SomeAbstractClass.class, dependencies[0]);
	}

	@Test
	public void testGetMaxArgsConstructor_null() {
		try {
			ReflectionsUtils.getMaxArgsConstructor(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void getMaxArgsConstructor_noConstructor() {
		Constructor<?> defaultConstructor = ReflectionsUtils.getMaxArgsConstructor(SomeAbstractClassImpl.class);
		assertEquals(0, defaultConstructor.getParameterCount());
	}

	@Test
	public void getMaxArgsConstructor_requiredConstructor() {
		Constructor<?> requiredConstructor = ReflectionsUtils.getMaxArgsConstructor(DependsOnSomeAbstractClass.class);
		assertEquals(1, requiredConstructor.getParameterCount());
	}

	@Test
	public void isInstatniable_null() {
		try {
			ReflectionsUtils.isInstantiable(null);
			fail();
		} catch (NullPointerException e) {
		}
	}
	
	@Test
	public void isInstantiable_abstractClass()
	{
		assertFalse(ReflectionsUtils.isInstantiable(SomeAbstractClass.class));
	}
	
	@Test
	public void isInstantiable_interface()
	{
		assertFalse(ReflectionsUtils.isInstantiable(ITestMockDao.class));
	}
	
}
