package de.hshannover.operation_muehle.utils;

import de.hshannover.operation_muehle.logic.Logger;

public abstract class TimeObserver {
	private boolean hasFinishedInTime;
	private Thread thread;
	
	public TimeObserver(int thinkTime) {
		thread = new Thread() {
			public void run() {
				doRun();
			}
		};
		thread.start();
		
		waitForThread(thinkTime);
		this.hasFinishedInTime = !thread.isAlive();
	}

	private void waitForThread(long thinkTime) {
		long startSysTime = System.currentTimeMillis();
		try {
			synchronized (thread) {
				thread.join(thinkTime);
			}
		} catch (InterruptedException e) {
			long elapsedTime = System.currentTimeMillis() - startSysTime;
			Logger.logDebugf("waited %d", elapsedTime);
			if ( elapsedTime < thinkTime) {
				waitForThread(thinkTime - elapsedTime);
			}
		}
		
	}
	
	private void doRun() {
		run();
		synchronized (thread) {
			thread.notifyAll();
		}
	}
	
	public boolean hasFinishedInTime() {
		return this.hasFinishedInTime;
	}
	
	public abstract void run();
}