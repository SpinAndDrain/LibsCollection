package de.spinanddrain.util;

import de.spinanddrain.util.advanced.AdvancedString;
import de.spinanddrain.util.arrays.ObjectArray;
import de.spinanddrain.util.holding.StaticMemorizer;

public final class Utils {
	
	static {
		container = new Utils();
	}
	
	private static final Utils container;
	
	private final Memory memory;
	
	private Utils() {
		this.memory = new Memory();
	}
	
	/**
	 * Creates and runs a new Thread.
	 * 
	 * @param a
	 * @return  id of the thread as Integer
	 */
	public static int async(Async a) {
		return container.memory.addAndStart(new Thread(()->{a.run(f->{});}));
	}
	
	/**
	 * 
	 * @return  local Memory
	 */
	public static Memory getMemory() {
		return container.memory;
	}

	/**
	 * Expands a normal String to a new AdvancedString.
	 * 
	 * @param string
	 * @return  AdvancedString::new(string)
	 */
	public static AdvancedString expand(String string) {
		return new AdvancedString(string);
	}
	
	public static class Memory {
		
		private final ObjectArray threads;
		private StaticMemorizer<?> memorizer;
		
		private Memory() {
			this.threads = new ObjectArray();
		}
		
		/**
		 * Stores the specified value in the static Memory.
		 * 
		 * @param <T>  type of value
		 * @param object  value to store
		 */
		public synchronized <T> void store(T object) {
			this.memorizer = new StaticMemorizer<T>(object);
		}
		
		/**
		 * <b>Note:</b> This value and its type can be changed by any Thread!
		 * 
		 * @param <T>  type of the currently stored value
		 * @return  returns the last stored value (can be null at any time)
		 */
		@SuppressWarnings("unchecked")
		public synchronized <T> T tryMemorizing() {
			return (T) this.memorizer.get();
		}
		
		/**
		 * 
		 * @param threadId
		 * @return  the Thread of the specified id
		 */
		public Thread getThreadById(int threadId) {
			return (Thread) this.threads.toArray()[--threadId];
		}
		
		private int addAndStart(Thread t) {
			this.threads.add(t);
			t.start();
			return this.threads.length();
		}
		
	}
	
}
