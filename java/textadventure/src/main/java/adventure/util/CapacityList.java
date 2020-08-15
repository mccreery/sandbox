package adventure.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A resizable list with a maximum capacity.
 * {@link #add(int, Object) add(int, T)} and related methods throw
 * {@link IllegalStateException} if the list would exceed its capacity as a
 * result.
 */
public class CapacityList<T> extends AbstractList<T> {
    private final T[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public CapacityList(int capacity) {
        elements = (T[]) new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    public <U extends T> CapacityList(Collection<U> collection, int capacity) {
        elements = (T[]) Arrays.copyOfRange(collection.toArray(), 0, capacity);
        size = Math.min(collection.size(), capacity);
        assert size <= capacity;
    }

    @Override
    public T get(int index) {
        return elements[index];
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return elements.length;
    }

    public int free() {
        return elements.length - size;
    }

    public boolean isFull() {
        assert size <= elements.length;
        return size == elements.length;
    }

    @Override
    public T set(int index, T element) {
        T previousElement = elements[index];
        elements[index] = element;
        return previousElement;
    }

    /**
     * @throws IllegalStateException if the list would exceed capacity as a
     * result of adding the element.
     */
    @Override
    public void add(int index, T element) {
        assert size <= elements.length;

        if (size == elements.length) {
            throw new IllegalStateException("reached capacity");
        }

        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        ++size;
    }

    /**
     * @throws IllegalStateException if the list would exceed capacity as a
     * result of adding the element.
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    /**
     * @throws IllegalStateException if the list would exceed capacity as a
     * result of adding the element.
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        assert size <= elements.length;

        if (c.size() > free()) {
            throw new IllegalStateException("reached capacity");
        }

        @SuppressWarnings("unchecked")
        T[] collectionArray = (T[]) c.toArray();

        System.arraycopy(elements, index, elements, index + c.size(), size - index);
        System.arraycopy(collectionArray, 0, elements, index, collectionArray.length);
        size += c.size();
        return true;
    }

    @Override
    public T remove(int index) {
        T previousElement = elements[index];
        removeRange(index, index + 1);
        return previousElement;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        if (toIndex < size) {
            System.arraycopy(elements, toIndex, elements, fromIndex, size - toIndex);
        }
        size -= toIndex - fromIndex;
    }
}
