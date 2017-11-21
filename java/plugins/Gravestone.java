package tk.nukeduck.Gravestone;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Gravestone extends JavaPlugin implements Listener {
	ItemStack result = new ItemStack(Material.SIGN, 1);
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		result.setDurability((short) 1);
		
		ItemMeta itemMeta = result.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + "Gravestone");
        
        itemMeta.setLore(Arrays.asList("Keeps your items safe"));
        result.setItemMeta(itemMeta);
		
		ShapedRecipe recipe = new ShapedRecipe(result);
		
		recipe.shape("BBB", "BSB", "OCO");
		recipe.setIngredient('B', Material.SMOOTH_BRICK);
		recipe.setIngredient('S', Material.SIGN);
		recipe.setIngredient('C', Material.CHEST);
		recipe.setIngredient('O', Material.OBSIDIAN);
		
		getServer().addRecipe(recipe);
	}
	
	public void onDisable() {
		
	}
	
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(player.getInventory().contains(result)) {
        	ArrayList<int[]> locations = new ArrayList<int[]>();
        	ArrayList<Float> distances = new ArrayList<Float>();
        	
        	World world = player.getWorld();
        	
        	for(int x = player.getLocation().getBlockX() - 5; x < player.getLocation().getBlockX() + 5; x++) {
        		for(int y = player.getLocation().getBlockY() - 5; y > player.getLocation().getBlockY() + 5; y++) {
        			for(int z = player.getLocation().getBlockZ() - 5; z < player.getLocation().getBlockZ() + 5; z++) {
        				//if(world.getBlockAt(x, y, z).isEmpty() && player.getWorld().getBlockAt(x + 1, y, z).isEmpty() && world.getBlockAt(x + 2, y, z).isEmpty()) {
	        				locations.add(new int[] {x, y, z});
	        				distances.add((float) Math.sqrt(Math.pow(x - player.getLocation().getX(), 2) + Math.pow(y - player.getLocation().getY(), 2) + Math.pow(z - player.getLocation().getZ(), 2)));
        				//}
        			}
        		}
        	}
        	
        	locations.add(new int[] {player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()});
        	distances.add(0.0F);
        	
        	int[] finalLocation = locations.get(min(distances));
        	
        	if(finalLocation != null) {
        		world.getBlockAt(finalLocation[0], finalLocation[1] - 1, finalLocation[2]).setType(Material.GRAVEL);
        		world.getBlockAt(finalLocation[0] + 1, finalLocation[1] - 1, finalLocation[2]).setType(Material.GRAVEL);
        		world.getBlockAt(finalLocation[0] + 2, finalLocation[1] - 1, finalLocation[2]).setType(Material.GRAVEL);
        		
        		world.getBlockAt(finalLocation[0], finalLocation[1], finalLocation[2]).setType(Material.SMOOTH_BRICK);
        		world.getBlockAt(finalLocation[0], finalLocation[1] + 1, finalLocation[2]).setType(Material.SMOOTH_STAIRS);
        		
        		world.getBlockAt(finalLocation[0] + 1, finalLocation[1] - 2, finalLocation[2]).setType(Material.CHEST);
        		world.getBlockAt(finalLocation[0] + 2, finalLocation[1] - 2, finalLocation[2]).setType(Material.CHEST);
        		
        		ItemStack[] inventory = new ItemStack[54];
        		for(int i = 0; i < player.getInventory().getContents().length; i++) {
        			inventory[i] = player.getInventory().getContents()[i];
        		}
        		for(int i = player.getInventory().getContents().length; i < player.getInventory().getContents().length + 4; i++) {
        			inventory[i] = player.getInventory().getArmorContents()[i - player.getInventory().getContents().length];
        		}
        		
        		Inventory c = ((Chest) (world.getBlockAt(finalLocation[0] + 1, finalLocation[1] - 2, finalLocation[2]).getState())).getInventory();
        		for(int i = 0; i < 9; i++) {
        			c.setItem(i + 45, inventory[i]);
        		}
        		for(int i = 9; i < 36; i++) {
        			c.setItem(i + 9, inventory[i]);
        		}
        		c.setItem(3, inventory[39]);
        		c.setItem(12, inventory[38]);
        		c.setItem(5, inventory[37]);
        		c.setItem(14, inventory[36]);
        		
        		world.getBlockAt(finalLocation[0] + 1, finalLocation[1] + 1, finalLocation[2]).setType(Material.WALL_SIGN);
        		Sign sign = (Sign) (world.getBlockAt(finalLocation[0] + 1, finalLocation[1] + 1, finalLocation[2]).getState());
        		
        		String s = String.format("%-60s", event.getDeathMessage());
        		
        		for(int i = 0; i < 4; i++) {
        			sign.setLine(i, s.substring(i * 15, (i * 15) + 15));
        		}
        		
        		sign.setRawData((byte) 5);
        		sign.update();
        		
        		event.getDrops().clear();
        	}
        }
    }
    
    private static int min(ArrayList<Float> floats) {
    	if(floats.size() == 0) return -1;
    	
    	float max = floats.get(0);
    	int nan = 0;
    	for (int ktr = 0; ktr < floats.size(); ktr++) {
    		if (floats.get(ktr) < max) {
    			max = floats.get(ktr);
    			nan = ktr;
    		}
    	}
    	return nan;
    }
}