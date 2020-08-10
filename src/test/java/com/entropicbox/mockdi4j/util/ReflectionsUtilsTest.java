package com.entropicbox.mockdi4j.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
			fail("Exception should have been thrown");
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
			fail("Exception should have been thrown");
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
}
