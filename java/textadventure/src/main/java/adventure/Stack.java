package adventure;

import adventure.glot.Glot;

public class Stack<T extends INamed> {
    private final T t;
    private final int count;

    public Stack(T t, int count) {
        this.t = t;

        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        this.count = count;
    }

    public String getDescription(Glot glot) {
        return glot.localize(t.getNameKey(), count);
    }
}
