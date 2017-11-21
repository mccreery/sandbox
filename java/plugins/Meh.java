package tk.nukeduck.Meh;

import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Meh extends JavaPlugin {
	public int radius = 10;
	public String message;
	
	public void onEnable() {
		getConfig().addDefault("radius", 10);
		getConfig().addDefault("message", "meh.");
		saveConfig();
		
		radius = getConfig().getInt("radius");
		message = getConfig().getString("message");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Location player = ((Player) sender).getLocation();
		
		if(cmd.getName().equalsIgnoreCase("meh") && sender instanceof Player) {
			for(Player p : getServer().getOnlinePlayers()) {
				if(p.getLocation().distance(player) <= radius && p.getName() != ((Player) sender).getName()) {
					p.setVelocity(p.getVelocity().setY(5));
					p.sendMessage(message);
				}
			}
			return true;
		}
		return false;
	}
}