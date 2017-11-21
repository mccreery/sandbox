package tk.nukeduck.FAQ;

import java.util.*;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class FAQ extends JavaPlugin implements Listener {
	String[][] slots; // Material name, Name, Description, Link
	
	static String guiName = "Frequently Asked Questions";
	static String setMessage = "Successfully added info on";
	static String recallMessage = "Info on";
	
	static int containerSize = 9;
	
	static String[] emptySlot = new String[] {"", "", "", ""};
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		FileConfiguration config = this.getConfig(); {
			config.options().copyDefaults(true);
			
			config.addDefault("title", "Frequently Asked Questions ");
			config.addDefault("setMessage", "Successfully set to ");
			config.addDefault("recallMessage", "Info on ");
			
			config.addDefault("slots", 9);
			
			for(int i = 0; i < config.getInt("slots"); i++) {
				config.addDefault("inventory." + i, "empty");
			}
		}
		this.saveConfig();
		
		guiName = ChatColor.translateAlternateColorCodes('&', config.getString("title"));
		setMessage = ChatColor.translateAlternateColorCodes('&', config.getString("setMessage"));
		recallMessage = ChatColor.translateAlternateColorCodes('&', config.getString("recallMessage"));
		containerSize = config.getInt("slots");
		
		slots = new String[containerSize][4];
		
		for(int i = 0; i < containerSize; i++) {
			String[] stackDetails = config.getString("inventory." + i).split(", ");
			
			if(stackDetails.length == 4) {
				slots[i] = stackDetails;
			} else {
				slots[i] = emptySlot;
			}
		}
		
		reloadInventory();
	}
	
	public void onDisable() {
		FileConfiguration config = this.getConfig();
		for(int i = 0; i < containerSize; i++) {
			if(!slots[i].equals(emptySlot)) {
				config.set("inventory." + i, slots[i][0] + ", " + slots[i][1] + ", " + slots[i][2] + ", " + slots[i][3]);
			}
		}
		this.saveConfig();
	}
	
	Inventory inv;
	
	public void reloadInventory() {
		inv = Bukkit.createInventory(null, containerSize, guiName);
		for(int i = 0; i < containerSize; i++) {
			if(!slots[i].equals(emptySlot)) {
				ItemStack stack = new ItemStack(Material.getMaterial(slots[i][0]), 1);
				ItemMeta meta = stack.getItemMeta();
				
				meta.setDisplayName(ChatColor.AQUA + ChatColor.translateAlternateColorCodes('&', slots[i][1].replace("_", " ")));
				
				List<String> lore = new ArrayList<String>();
				if(lore != null) lore.add(ChatColor.RESET + "" + ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', slots[i][2].replace("_", " ")));
				meta.setLore(lore);
				
				stack.setItemMeta(meta);
				inv.setItem(i, stack);
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("faq") && sender.hasPermission("FAQ.openMenu")) {
			((Player) sender).openInventory(inv);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("faqset") && sender.hasPermission("FAQ.edit")) {
			if(args.length >= 2) {
				if(!args[0].matches("-?\\d+")) {
					sender.sendMessage(ChatColor.RED + "Slot must be an integer.");
					return false;
				}
				
				if(Integer.parseInt(args[0]) < containerSize && Integer.parseInt(args[0]) >= 0) {
					if(args[1].equalsIgnoreCase("empty")) {
						slots[Integer.parseInt(args[0])] = emptySlot;
						sender.sendMessage(setMessage + " empty");
					} else if(args[1].equalsIgnoreCase("hand") && args.length >= 5) {
						if(!(sender instanceof Player)) {
							sender.sendMessage(ChatColor.RED + "Consoles don't have hands!");
							return false;
						}
						slots[Integer.parseInt(args[0])] = new String[] {((Player) sender).getItemInHand().getType().name(), args[2], args[3], args[4]};
						sender.sendMessage(setMessage + ((Player) sender).getItemInHand().getType().name() + ", " + ChatColor.translateAlternateColorCodes('&', args[2] + ", " + args[3] + ", " + args[4]));
					} else if(args.length >= 5) {
						if(interpret(args[1].toUpperCase()) == null) {
							sender.sendMessage(ChatColor.RED + "That is not a recognised block or item.");
							return false;
						}
						slots[Integer.parseInt(args[0])] = new String[] {interpret(args[1].toUpperCase()).name(), args[2], args[3], args[4]};
						sender.sendMessage(setMessage + interpret(args[1].toUpperCase()).name() + ", " + ChatColor.translateAlternateColorCodes('&', args[2] + ", " + args[3] + ", " + args[4]));
					}
					reloadInventory();
					return true;
				}
				sender.sendMessage(ChatColor.RED + "Slot must be from 0 to " + (containerSize - 1));
				return false;
			}
			sender.sendMessage(ChatColor.RED + "Not enough arguments.");
			return false;
		}
		sender.sendMessage(ChatColor.RED + "Invalid arguments.");
		return false;
	}
	
	public Material interpret(String str) {
		try {
			int id = Integer.parseInt(str);
			return Material.getMaterial(id);
		} catch(NumberFormatException e) {
			return Material.getMaterial(str);
		}
	}
	
	@EventHandler
	public void onItemInInvClick(InventoryClickEvent e) {
		if(!(e.getInventory().getName().equals(inv.getName()) && e.getInventory().getSize() == containerSize)) return;
		
		if(e.getSlot() >= 0 && e.getSlot() < containerSize && slots[e.getSlot()][0] != "") {
			((Player) e.getWhoClicked()).sendMessage(recallMessage + ChatColor.translateAlternateColorCodes('&', slots[e.getSlot()][1] + ": " + slots[e.getSlot()][3]));
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
		}
	}
}