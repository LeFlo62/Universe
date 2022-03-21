package fr.leflodu62.universe.utils;

public class ArrayUtils {
	
	/**
	 * Shifts elements from the array to the left.
	 * 
	 * @param <T> the class of the objects in the array
	 * @param array the array to shift elements
	 */
	public static final <T> void shiftLeft(T[] array) {
		int n = array.length;
		T first = array[0];
		for(int i = 0; i < n - 1; i++) {
			array[i] = array[i+1];
		}
		array[n-1] = first;
	}

	/**
	 * Shifts elements from the array to the right.
	 * 
	 * @param <T> the class of the objects in the array
	 * @param array the array to shift elements
	 */
	public static final <T> void shiftRight(T[] array) {
		int n = array.length;
		T last = array[n - 1];
		for(int i = n - 2; i >= 0; i--) {
			array[i + 1] = array[i];
		}
		array[0] = last;
	}

}
