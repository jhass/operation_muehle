package de.hshannover.operation_muehle.utils;


/** Utility to perform a task in the background
 * 
 * @author mrzyx
 *
 */
public abstract class PerformAsync implements Runnable {
	
	/** The task to perform in the background
	 * 
	 */
	public abstract void task();
	
	@Override
	public void run() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				task();
			}
		}).start();
	}

}
