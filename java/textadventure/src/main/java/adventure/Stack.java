package adventure;

import adventure.glot.Glot;
import adventure.item.Item;

public class Stack {
    private final Item item;
    private final int count;

    public Stack(Item item, int count) {
        this.item = item;

        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        this.count = count;
    }

    public String getDescription(Glot glot) {
        return glot.localize(item.getNameKey(), count);
    }
}
