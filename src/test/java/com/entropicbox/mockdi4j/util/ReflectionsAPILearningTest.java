package com.entropicbox.mockdi4j.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.reflections8.Reflections;
import org.reflections8.scanners.ResourcesScanner;
import org.reflections8.scanners.SubTypesScanner;
import org.reflections8.util.ClasspathHelper;
import org.reflections8.util.ConfigurationBuilder;
import org.reflections8.util.FilterBuilder;

public class ReflectionsAPILearningTest {

	@Before
	public void setup()
	{
	}
	
	@Test
	public void testInitReflections_packageDoesNotExist()
	{
		Reflections reflections = new Reflections("com.doesnotexist");
		assertNotNull(
				"Init'ing reflections on package which DNE does not throw exception", 
				reflections);
	}
	
	@Test
	public void testInitReflections_packageExists()
	{
		Reflections reflections = new Reflections("com.entropicbox.mockdi4j");
		assertNotNull(
				"Init'ing reflections on package which exists does not throw exception", 
				reflections);
	}
	
	@Test
	public void testGetAllClassesUnderPackage()
	{
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		Reflections reflections = 
			new Reflections(
				new ConfigurationBuilder()
				.setScanners(
					new SubTypesScanner(false /* don't exclude Object.class */), 
					new ResourcesScanner())
				.setUrls(
					ClasspathHelper.forClassLoader(
						classLoadersList.toArray(new ClassLoader[0])))
				.filterInputsBy(
					new FilterBuilder().include(
						FilterBuilder.prefix("com.entropicbox.mockdi4j"))));

		Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
		 
		 assertTrue(classes.size() > 0);

	}
	
	
}
