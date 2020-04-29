package de.spinanddrain.util;

public interface Async {
	
	void run(Async collection);
	
	default void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
