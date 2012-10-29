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
public class StrategyLoader {

	private ArrayList<String> availableClassNames = new ArrayList<String>();

	/**
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * 
	 */
	public StrategyLoader() throws IOException, SecurityException,
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
	public StrategyLoader(String jarDirName) throws MalformedURLException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		File directory = new File(jarDirName);
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(jarDirName + " is no directory");
		}

		File[] files = new File(jarDirName).listFiles();
		for (File file : files) {
			if (file.getName().endsWith(".jar")) {

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

				// second, read all classnames in jar-file
				ArrayList<String> classNamesInJar = this.readClassNames(file);
				this.availableClassNames.addAll(classNamesInJar);

			}
		}

	}

	private ArrayList<String> readClassNames(File file) {
		ArrayList<String> result = new ArrayList<String>();

		// that might be a jar-file
		JarFile jar;
		try {
			jar = new JarFile(file);
		} catch (IOException e) {
			System.out.println("File " + file.getAbsolutePath() + " is not a "
					+ "Jarfile");
			return null;
		}

		// notice all class-file entries
		JarEntry entry;
		for (Enumeration<JarEntry> entries = jar.entries();
				entries.hasMoreElements();) {
			entry = entries.nextElement();

			if (entry.getName().endsWith(".class")
					&& !entry.getName().contains("$")) {

				String className = entry.getName().replaceAll("/", ".")
						.replaceAll(".class", "");

				result.add(className);
			}
		}
		try {
			jar.close();
		} catch (IOException e) {
			System.out.println("Jarfile " + jar.getName()
					+ " could not be closed.");
		}

		return result;
	}

	public ArrayList<String> getClassNames() {
		return availableClassNames;
	}

	/**
	 * Get a list of class-objects that can be instantiated with a
	 * default constructor.
	 * 
	 * @param  type filter return
	 * @return 			
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ArrayList<Class<?>> getClasses(Class<?> type) {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		for (String klass : availableClassNames) {
			Class<?> c;
			try {
				c = Class.forName(klass);
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
				continue;
			}
			for (Class<?> i : c.getInterfaces()) {
				if (type == null || i.getName().equals(type.getName())) {
					continue;
				}
				classes.add(c);
			}
		}

		return classes;
	}

	/**
	 * Get a list of instances for all available strategies
	 * 
	 * @return a list of strategies
	 */
	public ArrayList<Strategy> getAllStrategies() {
		ArrayList<Strategy> strategies = new ArrayList<Strategy>();
		ArrayList<Class<?>> classes;

		classes = this.getClasses(Strategy.class);
		for (Class<?> cls : classes) {
			try {
				strategies.add((Strategy) cls.newInstance());
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}

		return strategies;
	}

	/**
	 * Gets an instance of the given class using the default constructor.
	 * 
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
	 * Returns the Class of the given classname
	 * 
	 * 
	 * @param klass
	 * @return the Class object for klass
	 * @throws ClassNotFoundException
	 *             if the class isn't available
	 */
	public Class<?> getClass(String klass) throws ClassNotFoundException {
		Class<?> result = null;
		for (String clsName : availableClassNames) {
			if (clsName.equals(klass)) {
				result = Class.forName(clsName);
			}
		}

		return result;
	}

}
