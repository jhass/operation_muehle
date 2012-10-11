package examples.observer;

import de.hshannover.operation_muehle.utils.observer.AObservable;
import de.hshannover.operation_muehle.utils.observer.IObserver;


class Observable extends AObservable {
	private String name = "Foo";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setObservableChanged(true);
	}
	
}

class Observer implements IObserver {
	private Observable observed;
	private String name;
	
	public Observer(Observable observed, String name) {
		this.name = name;
		this.observed = observed;
		this.observed.addObserver(this);
		System.out.printf("Observer %s got initial value: %s\n",
						  this.name, this.observed.getName());
	}
	
	@Override
	public void updateObservable() {
		System.out.printf("Observer %s got new value: %s\n",
						  this.name, this.observed.getName());
	}
	
}


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Observable observable = new Observable();
		observable.setName("Initial");
		
		new Observer(observable, "1");
		new Observer(observable, "2");
		
		
		Thread thread = new Thread() {
			public void run() {
				for (int i=0; i<10; i++) {
					observable.setName("New Name"+i);
					observable.notifyObserver();
				}
			}
		};
		
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {}
	}

}
