package com.entropicbox.mockdi4j.util;

import com.entropicbox.mockdi4j.exception.PackageNotFoundException;

public class ReflectionUtils {

	public static Package getBasePackage(String basePackageName) {
		if (basePackageName == null)
		{
			throw new NullPointerException();
		}
		
		ClassLoader currentClassLoader = ReflectionUtils.class.getClassLoader();
		Package basePackage = currentClassLoader.getDefinedPackage(basePackageName);

		if (basePackage == null)
			throw new PackageNotFoundException();
		
		return basePackage;
	}

}
