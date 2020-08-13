package com.entropicbox.mockdi4j.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections8.Reflections;
import org.reflections8.scanners.ResourcesScanner;
import org.reflections8.scanners.SubTypesScanner;
import org.reflections8.util.ClasspathHelper;
import org.reflections8.util.ConfigurationBuilder;
import org.reflections8.util.FilterBuilder;

import com.entropicbox.mockdi4j.exception.WireDependencyException;

public class ReflectionsUtils {

	public static Set<Class<?>> getImplementationClassesFromBasePackage(String basePackageName) {
		if (basePackageName == null)
			throw null;

		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
				.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(basePackageName))));

		Set<Class<?>> filteredClasses = ConcurrentHashMap.newKeySet();;
		for (Class<?> clazz : reflections.getSubTypesOf(Object.class)) {
			if (isInstantiable(clazz)) {
				filteredClasses.add(clazz);
			}
		}
		return filteredClasses;
	}

	public static boolean classDependsOn(Class<?> parent, Class<?> child) {
		if (parent == null || child == null)
			throw null;

		Class<?>[] dependentsOfParent = getDependentsOf(parent);

		return childSatisfiesAny(child, dependentsOfParent);
	}

	@SuppressWarnings("unchecked")
	public static <T> T newLeafInstance(Class<?> clazz) {
		if (clazz == null)
			throw null;
		try {
			Constructor<?> constructor = clazz.getConstructor();
			return (T) constructor.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new WireDependencyException();
		}

	}

	public static Class<?>[] getDependentsOf(Class<?> parent) {
		if (parent == null)
			throw null;

		Constructor<?> maxArgsConstructor = getMaxArgsConstructor(parent);

		// If there are no constructors...
		if (maxArgsConstructor == null) {
			return new Class<?>[0];// empty array
		}

		return maxArgsConstructor.getParameterTypes();
	}

	public static Constructor<?> getMaxArgsConstructor(Class<?> clazz) {
		// Find largest constructor
		int maxNumArgs = Integer.MIN_VALUE;
		Constructor<?> maxArgsConstrictor = null;
		for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			if (constructor.getParameterTypes().length > maxNumArgs) {
				maxNumArgs = constructor.getParameterTypes().length;
				maxArgsConstrictor = constructor;
			}
		}
		return maxArgsConstrictor;
	}

	public static boolean isInstantiable(Class<?> clazz) {
		if (clazz == null)
			throw null;
		return !clazz.isInterface() && 
				!Modifier.isAbstract(clazz.getModifiers()) &&
				!clazz.isEnum() &&
				!clazz.isAnnotation();
	}

	private static boolean childSatisfiesAny(Class<?> child, Class<?>[] dependentsOfParent) {
		for (Class<?> dependent : dependentsOfParent)
			if (dependent.isAssignableFrom(child))
				return true;

		return false;
	}
}
