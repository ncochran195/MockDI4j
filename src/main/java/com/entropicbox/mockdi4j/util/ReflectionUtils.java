package com.entropicbox.mockdi4j.util;

import java.util.ArrayList;
import java.util.List;

import com.entropicbox.mockdi4j.exception.PackageNotFoundException;

public class ReflectionUtils {

	private static final ClassLoader classLoader = getClassLoader();
	
	public static Package getBasePackage(String basePackageName) {
		if (basePackageName == null)
			throw new NullPointerException();
		
		Package basePackage = ReflectionUtils.classLoader.
				getDefinedPackage(basePackageName);

		if (basePackage == null)
			throw new PackageNotFoundException();
		
		return basePackage;
	}

	public static List<Package> getDescendentsFrom(Package rootPackage) {
		if (rootPackage == null)
			throw new NullPointerException();
		
		
		return getPackagesDefinedUnder(
				rootPackage, 
				ReflectionUtils.classLoader.getDefinedPackages());
	}

	private static ClassLoader getClassLoader() {
		return ReflectionUtils.class.getClassLoader();
	}
	
	private static List<Package> getPackagesDefinedUnder(
			Package rootPackage, 
			Package[] definedPackage)
	{
		List<Package> descendents = new ArrayList<Package>();
		
		for (Package packg : definedPackage)
			if (packg.getName().startsWith(rootPackage.getName()))
				descendents.add(packg);
		
		return descendents;
	}
}
