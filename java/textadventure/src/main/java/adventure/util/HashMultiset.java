package adventure.util;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HashMultiset<T> extends AbstractCollection<T> implements Multiset<T> {
    private final Map<Object, Integer> elements = new LinkedHashMap<>();
    private int size;

    public HashMultiset() {
    }

    public HashMultiset(Collection<? extends T> collection) {
        addAll(collection);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Iterator<Entry<Object, Integer>> mapIterator = elements.entrySet().iterator();

            private Entry<Object, Integer> entry;
            private int entryUsed;
            private int entryTotal;

            @Override
            public boolean hasNext() {
                if (entry == null || entryUsed >= entryTotal) {
                    if (mapIterator.hasNext()) {
                        entry = mapIterator.next();
                        entryUsed = 0;
                        entryTotal = entry.getValue();
                    } else {
                        return false;
                    }
                }
                return entryUsed < entryTotal;
            }

            @SuppressWarnings("unchecked")
            @Override
            public T next() {
                ++entryUsed;
                return (T)entry.getKey();
            }

            @Override
            public void remove() {
                int newValue = entry.getValue() - 1;

                if (newValue == 0) {
                    mapIterator.remove();
                } else {
                    entry.setValue(newValue);
                }
                --size;
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(T element, int count) {
        elements.merge(element, count, (a, b) -> a + b);
        size += count;
        return true;
    }

    @Override
    public boolean remove(Object element, int count) {
        int storedCount = elements.get(element);

        if (storedCount < count) {
            return false;
        } else if (storedCount == count) {
            elements.remove(element);
        } else {
            elements.put(element, storedCount - count);
        }
        return true;
    }

    /**
     * Removes all instances of the specified element from the set.
     * @return {@code true} if any elements were removed.
     */
    public int removeAll(Object element) {
        Integer count = elements.remove(element);

        if (count != null) {
            size -= count;
            return size;
        } else {
            return 0;
        }
    }

    /**
     * @return The number of elements in the set equal to {@code element}.
     */
    public int getCount(Object element) {
        return elements.getOrDefault(element, 0);
    }

    @Override
    public void clear() {
        elements.clear();
        size = 0;
    }

    @Override
    public boolean contains(Object element) {
        return elements.containsKey(element);
    }
}
