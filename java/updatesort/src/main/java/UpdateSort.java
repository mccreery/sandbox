import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class UpdateSort {
	private UpdateSort() {}

	/** {@code comparator} defaults to {@code null}
	 * @see #updateSort(List, Comparator, int) */
	public static <T> int updateSort(List<T> sortedList, int dirtyIndex) {
		return updateSort(sortedList, dirtyIndex, null);
	}

	/** Moves a single updated value in a previously sorted list to its correct sorted position
	 *
	 * @param sortedList The (almost) sorted list
	 * @param comparator The comparator used to sort the list
	 * @param dirtyIndex The index of the element
	 * @return The new index of the element */
	public static <T> int updateSort(List<T> sortedList, int dirtyIndex, Comparator<? super T> comparator) {
		T key = sortedList.get(dirtyIndex);

		int index = lowerBound(sortedList.subList(0, dirtyIndex), key, comparator);

		if(index == dirtyIndex) {
			index = dirtyIndex + 1 + lowerBound(sortedList.subList(dirtyIndex + 1, sortedList.size()), key, comparator);
		}
		return move(sortedList, dirtyIndex, index);
	}

	/** Finds an index of the least item in a sorted list that is not less than {@code key}
	 *
	 * @param sortedList A sorted list
	 * @param key The key to search for
	 * @param comparator The comparator used to sort the list. Can be {@code null}
	 * @return The found index
	 * @see Collections#binarySearch(List, Object, Comparator) */
	public static <T> int lowerBound(List<T> sortedList, T key, Comparator<? super T> comparator) {
		int i = Collections.binarySearch(sortedList, key, comparator);
		return i >= 0 ? i : -(i + 1);
	}

	/** Removes and inserts an item into a list without collapsing the empty space
	 * between operations. This means that the source is always moved immediately
	 * in front of the destination, regardless of their order
	 *
	 * @param list A list
	 * @param src Source index
	 * @param dest Destination index
	 * @return The new source index */
	public static <T> int move(List<T> list, int src, int dest) {
		if(dest > src) {
			// Move right
			Collections.rotate(list.subList(src, dest), -1);
			return dest - 1;
		} else if(dest < src) {
			// Move left
			Collections.rotate(list.subList(dest, src + 1), 1);
		}
		return dest;
	}
}
