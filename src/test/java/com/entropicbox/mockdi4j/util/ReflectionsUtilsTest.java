package com.entropicbox.mockdi4j.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.entropicbox.mockdi4j.MockDI;
import com.entropicbox.mockdi4j.exception.PackageNotFoundException;

public class ReflectionsUtilsTest {

	@Before
	public void setup()
	{
		//	This is here to load the base mockdi4j package into the classloader
		new MockDI();
	}
	
	@Test
	public void testGetBasePackage_notFound()
	{
		try 
		{
			ReflectionUtils.getBasePackage("com.doesnotexist");
			fail();
		}
		catch(PackageNotFoundException e)
		{}
	}
	
	@Test
	public void testGetBasePackage_null()
	{
		try
		{
			ReflectionUtils.getBasePackage(null);
			fail();
		}
		catch(NullPointerException e)
		{}
	}
	
	@Test
	public void testGetBasePackage_found()
	{
		Package basePackage = ReflectionUtils.getBasePackage("com.entropicbox.mockdi4j");
		assertNotNull(basePackage);
	}
	
	@Test
	public void getDescendentsFrom_null()
	{
		try
		{
			ReflectionUtils.getDescendentsFrom(null);
			fail();
		}
		catch(NullPointerException e)
		{}
	}
	
	@Test
	public void getDescendentsFrom_basePackage()
	{
		Package basePackage = ReflectionUtils.getBasePackage("com.entropicbox.mockdi4j");
		List<Package> descendents = ReflectionUtils.getDescendentsFrom(basePackage);
		assertTrue("At least 2 packages expected", descendents.size() > 1);
	}
		
}
