package adventure.item;

import adventure.INamed;

public class Item implements INamed {
    public static final Item LIT_TORCH = new Torch(true);
	public static final Item UNLIT_TORCH = new Torch(false);
    public static final Item FLINT = new Item("flint");

    private final String nameKey;

    public Item(String nameKey) {
        this.nameKey = nameKey;
    }

    @Override
    public String getNameKey() {
        return nameKey;
    }

    /**
     * @return The result of combining the two items, with {@code this} as the
     * primary ingredient.
     */
    protected Item combine(Item item) {
        return null;
    }

    /**
     * Attempts to combine the primary ingredient with the secondary ingredient,
     * then the reverse.
     *
     * @return The first successful combination.
     */
    public static Item combine(Item primary, Item secondary) {
        Item combination = primary.combine(secondary);
        if (combination == null) {
            combination = secondary.combine(primary);
        }
        return combination;
    }
}
