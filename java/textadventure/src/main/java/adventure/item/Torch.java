package adventure.item;

public class Torch extends Item {
    private boolean lit;

    public Torch(boolean lit) {
        super(lit ? "torch.lit" : "torch");
        this.lit = lit;
    }

    @Override
    protected Item combine(Item item) {
        if (!lit && item == Item.FLINT) {
            return Item.LIT_TORCH;
        } else {
            return null;
        }
    }
}
