package tk.nukeduck.HubToys;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HubToys extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		
	}
	
    @EventHandler
    public void toggle(PlayerInteractEvent event){
        Player player = event.getPlayer();
       
        if(player.getInventory().getItemInHand().getType() == Material.FIREBALL){
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            	for(int i = 0; i <= 8; i++) {
            		player.getWorld().spawnArrow(player.getEyeLocation().add(player.getLocation().getDirection()), player.getLocation().getDirection(), 2F, 0.0F);
            	}
            	event.setCancelled(true);
            }
        }
    }
}