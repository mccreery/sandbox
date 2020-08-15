package adventure.glot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class FunctionCache<K, V> {
    private final Map<K, V> cachedValues = new HashMap<>();
    private final Function<K, V> function;

    public FunctionCache(Function<K, V> function) {
        this.function = function;
    }

    public V get(K key) {
        return cachedValues.computeIfAbsent(key, function);
    }
}
