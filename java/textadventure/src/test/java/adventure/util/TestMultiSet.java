package adventure.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestMultiSet {
    private List<Integer> sortedRandomList;

    @BeforeEach
    public void beforeAll() {
        final int capacity = 100;

        sortedRandomList = new ArrayList<>(capacity);
        Random random = new Random(42);

        for (int i = 0; i < capacity; i++) {
            sortedRandomList.add(random.nextInt(10));
        }
        Collections.sort(sortedRandomList);
    }

    @Test
    public void empty() {
        Multiset<?> set = new Multiset<>();
        assertTrue(set.isEmpty());
        assertTrue(set.size() == 0);
    }

    @Test
    public void add() {
        Multiset<Object> set = new Multiset<>();
        Object element = new Object();

        for (int i = 0; i < 10; i++) {
            set.add(element);
            assertEquals(i + 1, set.size());
            assertEquals(i + 1, set.getCount(element));
        }
    }

    @Test
    public void emptyIterator() {
        Multiset<Object> set = new Multiset<>();
        assertTrue(!set.iterator().hasNext());
    }

    @Test
    public void iteratorLength() {
        Multiset<Integer> set = new Multiset<>();
        for (int i = 0; i < 10; i++) {
            set.add(i);
            set.add(i);
        }

        assertEquals(set.size(), exhaust(set.iterator()));
    }

    /**
     * Exhausts an iterator, discarding the elements.
     * @return The number of discarded elements.
     */
    private static int exhaust(Iterator<?> iterator) {
        int i = 0;
        while (iterator.hasNext()) {
            iterator.next();
            i++;
        }
        return i;
    }

    @Test
    public void iteratorValues() {
        Multiset<Integer> set = new Multiset<>(sortedRandomList);

        List<Integer> sortedSetList = new ArrayList<>(set);
        Collections.sort(sortedSetList);

        assertEquals(sortedRandomList, sortedSetList);
    }

    @Test
    public void iteratorRemove() {
        Multiset<Integer> set = new Multiset<>(sortedRandomList);

        sortedRandomList.removeIf(x -> x < 5);
        set.removeIf(x -> x < 5);

        List<Integer> sortedSetList = new ArrayList<>(set);
        Collections.sort(sortedSetList);

        assertEquals(sortedRandomList, sortedSetList);
    }

    @Test
    public void removeAll() {
        Multiset<Integer> set = new Multiset<>(sortedRandomList);

        while (sortedRandomList.remove((Integer)3)) ;
        set.removeAll(3);

        assertEquals(sortedRandomList.size(), set.size());
    }

    @Test
    public void containsRemove() {
        Multiset<Integer> set = new Multiset<>(sortedRandomList);
        assertTrue(!set.contains(10));

        final Integer element = 3;
        final int count = set.getCount(element);

        for (int i = 0; i < count; i++) {
            assertTrue(set.contains(element));
            assertEquals(count - i, set.getCount(element));
            assertTrue(set.remove(element));
        }

        assertTrue(!set.contains(element));
        assertTrue(!set.remove(element));

        assertTrue(!set.contains(10));
    }

    @Test
    public void clear() {
        Multiset<Integer> set = new Multiset<>(sortedRandomList);
        set.clear();
        assertTrue(set.isEmpty());
    }
}
