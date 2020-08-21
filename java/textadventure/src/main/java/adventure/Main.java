package adventure;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import adventure.glot.Glot;
import adventure.item.Item;

public class Main {
    public static void main(String[] arguments) {
        Glot textGlot = new Glot(ResourceBundle.getBundle("text"));
        List<Stack> inventory = new ArrayList<>(20);

        inventory.add(new Stack(Item.FLINT, 3));
        inventory.add(new Stack(Item.UNLIT_TORCH, 10));

        for (Stack stack : inventory) {
            System.out.println(stack.getDescription(textGlot));
        }
    }
}
