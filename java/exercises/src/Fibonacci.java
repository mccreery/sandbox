import java.util.ArrayList;

public class Fibonacci {
    private static final ArrayList<Long> fibs = new ArrayList<>();

    static {
        // Prefill fibonacci series
        long a = 0, b = 1;
        fibs.add(a);

        do {
            fibs.add(b);

            long temp = a + b;
            a = b;
            b = temp;
        } while(a <= b);

        fibs.trimToSize();
    }

    /**
     * The fibonacci sequence in O(1) time across the full long range.
     * Fibonacci is defined as:
     * <pre>f(0) = 0, f(1) = 1, f(n) = f(n-1) + f(n-2)</pre>
     *
     * @return The {@code n}th fibonacci number.
     */
    public static long fib(int n) {
        return fibs.get(n);
    }
}
