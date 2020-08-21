package adventure;

import java.text.Collator;
import java.util.Comparator;

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

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public String getDescription(Glot glot) {
        return glot.localize(item.getNameKey(), count);
    }

    public static Comparator<Stack> comparingName(Glot glot) {
        return Comparator.comparing(stack -> stack.getDescription(glot),
            Collator.getInstance(glot.getLocale()));
    }

    private static final Comparator<Stack> COMPARING_COUNT = Comparator.comparing(Stack::getCount);
    public static Comparator<Stack> comparingCount() {
        return COMPARING_COUNT;
    }
}
