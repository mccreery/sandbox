package tk.nukeduck.HeadDisguise;

import java.util.HashMap;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.*;

public class HeadDisguise extends JavaPlugin {
	private DisguiseAPI api;
	
	HashMap<String, ItemStack> heads = new HashMap<String, ItemStack>();
	
	@Override
	public void onEnable() {
		api = getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
        
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    public void run() {
		    	for(Player player : getServer().getOnlinePlayers()) {
		    		ItemStack helmet = player.getInventory().getHelmet();
		    		if(helmet != null && (!heads.containsKey(player.getName()) || heads.get(player.getName()).getDurability() != helmet.getDurability() || (helmet.getDurability() == 3 && ((SkullMeta) helmet.getItemMeta()).getOwner() != ((SkullMeta) heads.get(player.getName()).getItemMeta()).getOwner()))) {
		    			heads.put(player.getName(), helmet);
			    		api.undisguiseToAll(player);
			    		if(helmet != null && helmet.getType() == Material.SKULL_ITEM) {
			    			switch(helmet.getDurability()) {
			    				case 0:
					    			api.disguiseToAll(player, new MobDisguise(DisguiseType.SKELETON, true));
					    			player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "HeadDisguise" + ChatColor.DARK_RED + "]" + ChatColor.GRAY + " You've been disguised as a " + ChatColor.BLUE + "Skeleton" + ChatColor.GRAY + "!");
					    			break;
			    				case 1:
					    			api.disguiseToAll(player, new MobDisguise(DisguiseType.WITHER_SKELETON, true));
					    			player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "HeadDisguise" + ChatColor.DARK_RED + "]" + ChatColor.GRAY + " You've been disguised as a " + ChatColor.BLUE + "Wither Skeleton" + ChatColor.GRAY + "!");
					    			break;
			    				case 2:
					    			api.disguiseToAll(player, new MobDisguise(DisguiseType.ZOMBIE, true));
					    			player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "HeadDisguise" + ChatColor.DARK_RED + "]" + ChatColor.GRAY + " You've been disguised as a " + ChatColor.BLUE + "Zombie" + ChatColor.GRAY + "!");
					    			break;
			    				case 3: 
					    			SkullMeta meta = (SkullMeta) player.getInventory().getHelmet().getItemMeta();
					    			api.disguiseToAll(player, new PlayerDisguise(meta.getOwner(), false));
					    			player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "HeadDisguise" + ChatColor.DARK_RED + "]" + ChatColor.GRAY + " You've been disguised as " + ChatColor.BLUE + meta.getOwner() + ChatColor.GRAY + "!");
					    			break;
			    				case 4:
					    			api.disguiseToAll(player, new MobDisguise(DisguiseType.CREEPER, true));
					    			player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "HeadDisguise" + ChatColor.DARK_RED + "]" + ChatColor.GRAY + " You've been disguised as a " + ChatColor.BLUE + "Creeper" + ChatColor.GRAY + "!");
					    			break;
			    			}
			    		} else {
			    			player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "HeadDisguise" + ChatColor.DARK_RED + "]" + ChatColor.GRAY + " You've been undisguised.");
			    		}
		    		} else {
		    			heads.remove(helmet);
		    		}
		    	}
		    }
		}, 0, 10);
	}
	
	public void onDisable() {
		
	}
}