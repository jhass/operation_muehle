package de.hshannover.operation_muehle.utils.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import java.net.MalformedURLException;
import java.net.URL;

import de.hshannover.inform.muehle.strategy.Strategy;

/**
 * Loads any found strategies
 * 
 * @author MrZYX
 * @author zyklos
 */
public class ClassFileLoader {

	private ArrayList<String> availableClasses = new ArrayList<String>();

	/**
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * 
	 */
	public ClassFileLoader() throws IOException, SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		this("lib");
	}

	/**
	 * @throws MalformedURLException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * 
	 */
	public ClassFileLoader(String jarDir) throws MalformedURLException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		File directory = new File(jarDir);
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(jarDir + " is no directory");
		}

		File[] files = new File(jarDir).listFiles();
		for (File file : files) {
			if (file.getName().endsWith(".jar")) {
				// that might be a jar-file
				JarFile jar;
				try {
					jar = new JarFile(file);
				} catch (IOException e) {
					System.out.println("File " + file.getAbsolutePath()
							+ " is not a " + "Jarfile");
					continue;
				}

				// first add that jar to the existing resource-heap
				URL jarFile = new URL("jar", "", "file:"
						+ file.getAbsolutePath() + "!/");

				URLClassLoader cl = (URLClassLoader) java.lang.ClassLoader
						.getSystemClassLoader();

				Class<URLClassLoader> sysClass = URLClassLoader.class;
				Method sysMethod = sysClass.getDeclaredMethod("addURL",
						new Class[] { URL.class });
				sysMethod.setAccessible(true);
				sysMethod.invoke(cl, new Object[] { jarFile });

				// notice all class-file entries
				JarEntry entry;
				for (Enumeration<JarEntry> entries = jar.entries(); entries
						.hasMoreElements();) {
					entry = entries.nextElement();

					if (entry.getName().endsWith(".class")
							&& !entry.getName().contains("$")) {

						String className = entry.getName().replaceAll("/", ".")
								.replaceAll(".class", "");

						availableClasses.add(className);
					}
				}
				try {
					jar.close();
				} catch (IOException e) {
					System.out.println("Jarfile " + jar.getName()
							+ " could not be closed.");
				}

			}
		}

		// now, we can go for all class-files

	}

	public ArrayList<String> getClassNames() {
		return availableClasses;
	}

	/**
	 * Get a list of instances for all classes that can be instantiated with a
	 * default constructor.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ArrayList<Object> getClassesOfType(Class<?> type)
			throws ClassNotFoundException {
		ArrayList<Object> instances = new ArrayList<Object>();

		for (String klass : availableClasses) {
			Class<?> c;
			c = Class.forName(klass);
			for (Class<?> i : c.getInterfaces()) {
				if (type == null || i.getName().equals(type.getName())) {
					continue;
				}
				instances.add(c);
			}
		}

		return instances;
	}

	/**
	 * Get a list of instances for all available strategies
	 * 
	 * @deprecated this method might be redundant.
	 * @return a list of strategies
	 */
	public ArrayList<Strategy> getAllStrategies() {
		ArrayList<Strategy> strategies = new ArrayList<Strategy>();

		/*
		 * for (Object strategy : this.getStrategies()) { try {
		 * strategies.add((Strategy) strategy); } catch (ClassCastException e) {
		 * } }
		 */

		return strategies;
	}

	/**
	 * Gets an instance of the given class using the default constructor.
	 * 
	 * @deprecated maybe too small for being a method
	 * @param klass
	 *            the name of the class
	 * @return The instance
	 * @throws ClassNotFoundException
	 *             if the given class isn't available
	 * @throws InstantiationException
	 *             if the given class can't be instantiated
	 * @throws IllegalAccessException
	 *             if the given class isn't public
	 */
	public Object getInstance(String klass) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		return Class.forName(klass).newInstance();
	}

	/**
	 * Returns the Class of the given class
	 * 
	 * @deprecated maybe too small for being a method
	 * @param klass
	 * @return the Class object for klass
	 * @throws ClassNotFoundException
	 *             if the class isn't available
	 */
	private Class<?> getClass(String klass) throws ClassNotFoundException {
		if (availableClasses.contains(klass)) {
			return Class.forName(klass);
		} else {
			throw new ClassNotFoundException("Can't find " + klass);
		}
	}

}
