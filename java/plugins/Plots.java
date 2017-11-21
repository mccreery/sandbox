package tk.nukeduck.Plots;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sign;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.BlockVector;
import com.earth2me.essentials.api.Economy;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Plots extends JavaPlugin implements Listener {
	public List<String> plots = new ArrayList<String>();
	
	@Override
	public void onEnable() {
		if(this.getConfig().getStringList("plots") != null) {
			plots = this.getConfig().getStringList("plots");
		} else {
			this.getConfig().set("plots", plots);
		}
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		this.getConfig().set("plots", plots);
	    this.saveConfig();
	}
	
	@EventHandler
	public void onPlaceBlock(SignChangeEvent e) {
		int[][] area = getArea(e);
		if(e.getLine(0).equalsIgnoreCase("[Plot]") && isPrice(e.getLine(1)) && area != null) {
			plots.add(area[0][0] + "/" + area[0][1] + "/" + area[0][2] + "/" + area[1][0] + "/" + area[1][1] + "/" + area[1][2] + "/" + e.getBlock().getX() + "/" + e.getBlock().getY() + "/" + e.getBlock().getZ());
			
			e.setLine(1, ChatColor.BLUE + "[Plot]");
			e.setLine(4, ChatColor.GRAY + e.getPlayer().getName());
			e.getPlayer().sendMessage(ChatColor.GREEN + "Plot successfully created at X: " + area[0][0] + ", Y: " + area[0][1] + ", Z: " + area[0][2] + " and X: " + area[1][0] + ", Y: " + area[1][1] + ", Z: " + area[1][2] + ".");
		} else if(e.getLine(0).equalsIgnoreCase("[Plot]")) {
			e.getPlayer().sendMessage(ChatColor.RED + "Plot could not be created. " + (isPrice(e.getLine(1)) ? "Couldn't find plot area." : "Sign is not valid."));
		}
	}
	
	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.WALL_SIGN) {
			String nan = null;
			for(String s : plots) {
				if(s.split("/")[6] == String.valueOf(e.getClickedBlock().getX()) && s.split("/")[7] == String.valueOf(e.getClickedBlock().getY()) && s.split("/")[8] == String.valueOf(e.getClickedBlock().getZ())) {
					nan = s;
				}
			}
			
			if(nan != null) {
				org.bukkit.block.Sign s = (org.bukkit.block.Sign) e.getPlayer().getWorld().getBlockAt(Integer.parseInt(nan.split("/")[6]), Integer.parseInt(nan.split("/")[7]), Integer.parseInt(nan.split("/")[8])).getState();
				
				boolean flag = true;
				switch(getPriceType(s.getLine(1))) {
					case 1:
						if(e.getPlayer().getInventory().contains(Material.getMaterial(s.getLine(1).toUpperCase()), 1)) {
							e.getPlayer().getInventory().removeItem(new ItemStack[] {new ItemStack(Material.getMaterial(s.getLine(1).toUpperCase()), 1)});
						} else if(e.getPlayer().getInventory().contains(Material.getMaterial(s.getLine(1).toUpperCase().replace("_", "")), 1)) {
							e.getPlayer().getInventory().removeItem(new ItemStack[] {new ItemStack(Material.getMaterial(s.getLine(1).toUpperCase().replace("_", "")), 1)});
						} else {
							flag = false;
						}
						break;
					case 2:
						if(e.getPlayer().getInventory().contains(Material.getMaterial(s.getLine(1).split(" ")[1].toUpperCase()), Integer.parseInt(s.getLine(1).split(" ")[0]))) {
							e.getPlayer().getInventory().removeItem(new ItemStack[] {new ItemStack(Material.getMaterial(s.getLine(1).split(" ")[1].toUpperCase()), Integer.parseInt(s.getLine(1).split(" ")[0]))});
						} else if(e.getPlayer().getInventory().contains(Material.getMaterial(s.getLine(1).split(" ")[1].replace("_", "_").toUpperCase()), Integer.parseInt(s.getLine(1).split(" ")[0]))) {
							e.getPlayer().getInventory().removeItem(new ItemStack[] {new ItemStack(Material.getMaterial(s.getLine(1).split(" ")[1].replace("_", "_").toUpperCase()), Integer.parseInt(s.getLine(1).split(" ")[0]))});
						} else {
							flag = false;
						}
						break;
					case 3:
						double cost = Double.parseDouble(s.getLine(1).substring(getNumberIndex(s.getLine(1))));
						try {
							if(Economy.hasEnough(e.getPlayer().getName(), cost)) {
								Economy.subtract(e.getPlayer().getName(), cost);
							} else {
								flag = false;
							}
						} catch (Exception e1) {
							flag = false;
						}
						break;
					default:
						flag = false;
						break;
				}
				
				if(flag) {
					WorldGuardPlugin plugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
					
					ProtectedCuboidRegion r = new ProtectedCuboidRegion(e.getPlayer().getName() + "'s Plot", new BlockVector(Integer.parseInt(nan.split("/")[0]), Integer.parseInt(nan.split("/")[1]), Integer.parseInt(nan.split("/")[2])), new BlockVector(Integer.parseInt(nan.split("/")[3]), Integer.parseInt(nan.split("/")[4]), Integer.parseInt(nan.split("/")[5])));
					r.getOwners().addPlayer(e.getPlayer().getName());
					
					plugin.getRegionManager(e.getPlayer().getWorld()).addRegion(r);
					
					s.setLine(0, "");
					s.setLine(1, "Owned by");
					s.setLine(2, e.getPlayer().getName());
				}
			}
		}
	}
	
	public int[][] getArea(SignChangeEvent e) {
		Sign signData = (Sign) e.getBlock().getState().getData();
		
		int[][] area = new int[2][3];
		int x = e.getBlock().getX(), y = e.getBlock().getY(), z = e.getBlock().getZ();
		
		switch(signData.getFacing()) {
			case NORTH: // Z-
				z++;
				while(e.getBlock().getWorld().getBlockAt(x - 1, y, z).getType() == Material.FENCE) x--;
				area[0] = new int[] {x, y, z};
				
				while(e.getBlock().getWorld().getBlockAt(x, y, z + 1).getType() == Material.FENCE) z++;
				while(e.getBlock().getWorld().getBlockAt(x + 1, y, z).getType() == Material.FENCE) x++;
				area[1] = new int[] {x, y, z};
				
				break;
			case EAST: // X+
				x--;
				while(e.getBlock().getWorld().getBlockAt(x, y, z - 1).getType() == Material.FENCE) z--;
				while(e.getBlock().getWorld().getBlockAt(x - 1, y, z).getType() == Material.FENCE) x--;
				area[0] = new int[] {x, y, z};
				
				while(e.getBlock().getWorld().getBlockAt(x, y, z + 1).getType() == Material.FENCE) z++;
				while(e.getBlock().getWorld().getBlockAt(x + 1, y, z).getType() == Material.FENCE) x++;
				area[1] = new int[] {x, y, z};
				
				break;
			case SOUTH: // Z+
				z--;
				while(e.getBlock().getWorld().getBlockAt(x - 1, y, z).getType() == Material.FENCE) x--;
				while(e.getBlock().getWorld().getBlockAt(x, y, z - 1).getType() == Material.FENCE) z--;
				area[0] = new int[] {x, y, z};
				
				while(e.getBlock().getWorld().getBlockAt(x + 1, y, z).getType() == Material.FENCE) x++;
				while(e.getBlock().getWorld().getBlockAt(x, y, z + 1).getType() == Material.FENCE) z++;
				area[1] = new int[] {x, y, z};
				
				break;
			case WEST: // X-
				x++;
				while(e.getBlock().getWorld().getBlockAt(x, y, z - 1).getType() == Material.FENCE) z--;
				area[0] = new int[] {x, y, z};
				
				while(e.getBlock().getWorld().getBlockAt(x + 1, y, z).getType() == Material.FENCE) x++;
				while(e.getBlock().getWorld().getBlockAt(x, y, z + 1).getType() == Material.FENCE) z++;
				area[1] = new int[] {x, y, z};
				
				break;
			default:
				return null;
		}
		
		return area[0][0] == area[1][0] || area[0][1] == area[1][1] || area[0][2] == area[1][2] ? null : area;
	}
	
	public boolean isPrice(String line) {
		String[] parts = line.split(" ");
		return (Material.getMaterial(line.toUpperCase()) != null || Material.getMaterial(line.replace("_", "").toUpperCase()) != null) || // Check if just an item (assumes one)
			(parts.length == 2 && endsWithNumber(parts[0]) && !parts[0].contains(".") && Material.getMaterial(parts[1].toUpperCase()) != null || Material.getMaterial(parts[1].replace("_", "").toUpperCase()) != null) || // check if item and amount
			(parts.length == 1 && endsWithNumber(parts[0])); // Check if numeric price
	}
	
	public int getPriceType(String line) {
		String[] parts = line.split(" ");
		return (Material.getMaterial(line.toUpperCase()) != null || Material.getMaterial(line.replace("_", "").toUpperCase()) != null) ? 1 : // Check if just an item (assumes one)
			(parts.length != 1 && endsWithNumber(parts[0]) && !parts[0].contains(".") && Material.getMaterial(parts[1].toUpperCase()) != null || Material.getMaterial(parts[1].replace("_", "").toUpperCase()) != null) ? 2 : // check if item and amount
			(parts.length == 1 && endsWithNumber(parts[0])) ? 3 : 0; // Check if numeric price
	}
	
	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	public static boolean endsWithNumber(String line) {
		for(int i = 0; i < line.length(); i++) {
			if(isNumeric(line.substring(i))) return true;
		}
		return false;
	}
	
	public static int getNumberIndex(String line) {
		for(int i = 0; i < line.length(); i++) {
			if(isNumeric(line.substring(i))) return i;
		}
		return -1;
	}
}