package com.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class Test {
	public static void main(String... args) throws Exception{
		CustomClassLoader loader1 = new CustomClassLoader(true);
		CustomClassLoader loader2 = new CustomClassLoader(false);
		startApplication(loader1,"com.main.Main","main","test1");
		startApplication(loader2,"com.main.Main","main","test2");
	}
	public static Object startApplication(ClassLoader classloader, String startClass, String startFunction, 
			String... startParam) throws Exception {
		Class<?> loadClass = classloader.loadClass(startClass);
		Method method = getMethod(loadClass, startFunction);
		Object startObj = loadClass.newInstance();
		method.invoke(startObj, (Object) startParam);
		return startObj;
	}
	 public static Method getMethod(Class<?> classObj, String methodName) throws NoSuchMethodException, SecurityException {
			Class<?> paramTypes[] = new Class[0];
			for (Method method : classObj.getDeclaredMethods()) {
				if (method.getName().equals(methodName)) {
					paramTypes = method.getParameterTypes();
					break;
				}
			}
			return classObj.getDeclaredMethod(methodName, paramTypes);
		}
}
