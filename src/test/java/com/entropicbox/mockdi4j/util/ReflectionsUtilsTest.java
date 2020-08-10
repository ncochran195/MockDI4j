package com.entropicbox.mockdi4j.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;

public class ReflectionsUtilsTest {

	@Test
	public void testGetClassesFromBasePackage_null()
	{
		try
		{
			ReflectionsUtils.getClassesFromBasePackage(null);
			fail();
		}
		catch(NullPointerException e)
		{}
	}
	
	@Test
	public void testGetClassesFromBasePackage_doesNotExist()
	{
		Set<Class<?>> classes = ReflectionsUtils.getClassesFromBasePackage("com.doesnotexist");
		
		assertTrue(classes.isEmpty());
	}
	
	@Test
	public void testGetClassesFromBasePackage_exists()
	{
		Set<Class<?>> classes = 
				ReflectionsUtils.getClassesFromBasePackage("com.entropicbox.mockdi4j");
		
		assertTrue("Expecting at least this test class and the class under test", 
				classes.size() >= 2);
	}

}
