package com.test;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Test {
	public static void main(String... args) throws Exception{
		/*String classpath = System.getProperty("java.class.path");
		for(String path:classpath.split(File.pathSeparator)){
			URL url = new File(path).toURI().toURL();
			System.out.println(url);
		}
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		System.out.println(urlClassLoader.toString());*/
		String fileName = "school";
		Student objName = new Student("bupt"); 
		
		System.out.println(getProperty(objName, fileName));
	}
	
	public static Object getProperty(Object obj,String fieldName) throws Exception{
		Class objClass = obj.getClass();
		Field field = objClass.getField(fieldName);
		Object property = field.get(obj);
		return property;
	}
}
