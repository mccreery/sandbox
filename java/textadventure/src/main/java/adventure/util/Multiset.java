package adventure.util;

import java.util.Collection;

public interface Multiset<T> extends Collection<T> {
    /**
     * @return The number of elements equal to {@code element}.
     */
    int getCount(Object element);

    /**
     * @return {@code true} if the elements were added.
     */
    boolean add(T element, int count);

    /**
     * Removes a number of elements, only if there at least that many.
     * @return {@code true} if the elements were removed.
     */
    boolean remove(Object element, int count);

    /**
     * @return The previous number of elements equal to {@code element}.
     */
    int removeAll(Object element);
}
