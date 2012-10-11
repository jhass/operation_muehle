package de.hshannover.operation_muehle.utils.observer;

import java.util.ArrayList;

public abstract class AObservable {
	private ArrayList<IObserver> observer = new ArrayList<IObserver>();
	private boolean observableChanged = false;
	
	/** Add an observer to be notified on changes
	 * 
	 * @param observer
	 */
	public void addObserver(IObserver observer) {
		this.observer.add(observer);
	}
	
	/** Remove an observer previously added
	 * 
	 * @param observer
	 */
	public void removeObserver(IObserver observer) {
		this.observer.remove(observer);
	}
	
	/** Notify all observer about changes if changed
	 * 
	 */
	public void notifyObserver() {
		if (this.observableChanged) {
			for (IObserver observer : this.observer) {
				observer.updateObservable();
			}
			
			this.setObservableChanged(false);
		}
		
	}
	
	/** Set whether the object has new changes for it observers
	 * 
	 * @param changed
	 */
	public void setObservableChanged(boolean changed) {
		this.observableChanged = changed;
	}
}
