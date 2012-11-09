package de.hshannover.operation_muehle.utils.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/** Generic class to fetch classes from JARs in a directory
 * 
 * @author Jonne Haß
 *
 */
public class ClassesLoader {
	private ArrayList<String> availableClasses = new ArrayList<String>();
	
	
	/** Load all JARs in searchPath (not recursive!)
	 * 
	 * @param searchPath directory to search for JARs
	 * @throws IOException If there's an error reading one of the JARs
	 * @throws IllegalArgumentException if searchPath is not a directory
	 */
	public ClassesLoader(String searchPath)
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
				
				try {
					addSoftwareLibrary(file);
				} catch (Exception e) {
					continue;
				}
				
				for (Enumeration<JarEntry> entries = jar.entries();
						entries.hasMoreElements();) {
					entry = entries.nextElement();
					if (entry.getName().endsWith(".class")) {
						availableClasses.add(entry.getName()
								.replaceAll("/", ".")
								.replaceAll(".class", ""));
					}
				}
			
				jar.close();
			}
		}
	}
	
	private void addSoftwareLibrary(File file) throws Exception {
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
		method.setAccessible(true);
		method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
	}
	
	/** Get a list of instances for all classes that can
	 *  be instantiated with a default constructor.
	 * 
	 * @return
	 */
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
	
	
	/** Gets an instance of the given class using the default
	 *  constructor.
	 * 
	 * @param klass the name of the class
	 * @return The instance
	 * @throws ClassNotFoundException if the given class isn't available
	 * @throws InstantiationException if the given class can't be instantiated
	 * @throws IllegalAccessException if the given class isn't public
	 */
	public Object getInstance(String klass)
			throws ClassNotFoundException,
			       InstantiationException,
			       IllegalAccessException {
		return getClass(klass).newInstance();
	}
	
	/** Returns the Class of the given class
	 * 
	 * @param klass
	 * @return the Class object for klass
	 * @throws ClassNotFoundException if the class isn't available
	 */
	private Class<?> getClass(String klass)
		throws ClassNotFoundException {
		if (availableClasses.contains(klass)) {
			return Class.forName(klass);
		} else {
			throw new ClassNotFoundException("Can't find "+klass);
		}
	}
	
}
