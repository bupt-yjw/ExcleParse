package com.main;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;


@SuppressWarnings("restriction")
public class CustomClassLoader extends URLClassLoader {

	public ConcurrentHashMap<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();

	// private static Logger logger = Logger.getLogger(CustomClassLoader.class);


	public CustomClassLoader(boolean useClassPath) {
		super(new URL[] {}, ClassLoader.getSystemClassLoader());
		if (!useClassPath) {
		}
	}
}
