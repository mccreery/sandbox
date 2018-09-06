import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class UpdateSortTest {
    private static final int SIZE = 100;

    private final Random random = new Random(0);
    private final List<Integer> list = new ArrayList<>(SIZE);

    @Before
    public void before() {
        random.setSeed(0);

        for(int i = 0; i < SIZE; i++) {
            list.add(i, random.nextInt());
        }
        Collections.sort(list);

        Assume.assumeTrue(list.size() == SIZE);
        Assume.assumeTrue(isSorted(list));
    }

    @Test
    public void testInsertionSort() {
        Collections.shuffle(list);
        Assume.assumeFalse(isSorted(list));

        for(int i = 0; i < list.size(); i++) {
            UpdateSort.updateSort(list.subList(0, i + 1), i);
        }
        Assert.assertTrue(isSorted(list));
    }

    @Test
    public void testFirstToLast() {
        testMove(0, list.size() - 1);
    }

    @Test
    public void testLastToFirst() {
        testMove(list.size() - 1, 0);
    }

    @Test
    public void testNoMove() {
        for(int i = 0; i < list.size(); i++) {
            testMove(i, i);
        }
    }

    @Test
    public void testSequential() {
        Collections.reverse(list);
        Assume.assumeFalse(isSorted(list));

        for(int i = 0; i < list.size(); i++) {
            UpdateSort.updateSort(list, i);
        }
        Assert.assertTrue(isSorted(list));
    }

    @Test
    public void testAll() {
        for(int i = 0; i < list.size(); i++) {
            for(int j = 0; j < list.size(); j++) {
                testMove(i, j);
            }
        }
    }

    private void testMove(int i, int j) {
        if(i != j) {
            list.add(j, list.remove(i));
            Assume.assumeFalse(isSorted(list));
        }
        UpdateSort.updateSort(list, j);
        Assert.assertTrue(isSorted(list));
    }

    private static <T extends Comparable<? super T>> boolean isSorted(Iterable<T> iterable) {
        return isSorted(iterable, Comparator.naturalOrder());
    }

    private static <T> boolean isSorted(Iterable<T> iterable, Comparator<T> comparator) {
        Iterator<T> it = iterable.iterator();
        if(!it.hasNext()) return true;
        T prev = it.next();

        while(it.hasNext()) {
            if(comparator.compare(prev, prev = it.next()) > 0) {
                return false;
            }
        }
        return true;
    }
}
