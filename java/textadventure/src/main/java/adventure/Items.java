package adventure;

import adventure.item.Item;
import adventure.item.Torch;

public class Items {
    public static final Item FLINT = new Item("flint");
    public static final Item UNLIT_TORCH = new Torch(false);
    public static final Item LIT_TORCH = new Torch(true);
}
