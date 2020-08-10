package com.entropicbox.mockdi4j.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.reflections8.Reflections;
import org.reflections8.scanners.ResourcesScanner;
import org.reflections8.scanners.SubTypesScanner;
import org.reflections8.util.ClasspathHelper;
import org.reflections8.util.ConfigurationBuilder;
import org.reflections8.util.FilterBuilder;

public class ReflectionsUtils {

	public static Set<Class<?>> getClassesFromBasePackage(String basePackageName) {
		if (basePackageName == null)
			throw new NullPointerException();

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
						FilterBuilder.prefix(basePackageName))));

		return reflections.getSubTypesOf(Object.class);
	}

}
