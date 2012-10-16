package de.hshannover.operation_muehle.utils.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoader {
	private ArrayList<String> availableClasses = new ArrayList<String>();
	
	
	public ClassLoader(String searchPath)
		throws IOException {
		File directory = new File(searchPath);
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(searchPath+" is no directory");
		}
		
		File[] files = new File(searchPath).listFiles();
		for (File file : files) {
			if (file.getName().endsWith(".jar")) {
				JarFile jar = new JarFile(file);
				JarEntry entry; 
			
				for (Enumeration<JarEntry> entries = jar.entries();
						entries.hasMoreElements();) {
					entry = entries.nextElement();
					if (entry.getName().endsWith(".class")) {
						entry = entries.nextElement();
						availableClasses.add(entry.getName()
								.replaceAll("/", ".")
								.replaceAll(".class", ""));
					}
				}
			
				jar.close();
			}
		}
	}
	
	public ArrayList<Object> getAllInstances() {
		ArrayList<Object> instances = new ArrayList<Object>();
		
		for (String klass : availableClasses) {
			try {
				instances.add(getInstance(klass));
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
		
		
		return instances;
	}
	
	public Object getInstance(String klass)
			throws ClassNotFoundException,
			       InstantiationException,
			       IllegalAccessException {
		return getClass(klass).newInstance();
	}
	
	private Class<?> getClass(String klass)
		throws ClassNotFoundException {
		if (availableClasses.contains(klass)) {
			return Class.forName(klass);
		} else {
			throw new ClassNotFoundException("Can't find "+klass);
		}
	}
	
}
