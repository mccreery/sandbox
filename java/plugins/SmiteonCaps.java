package tk.nukeduck.smitecaps;

import org.bukkit.command.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SmiteonCaps extends JavaPlugin implements Listener {
	public boolean enabled = true;
	
	@Override
	public void onEnable() {
		if(this.getConfig().isBoolean("enabled")) {
			enabled = this.getConfig().getBoolean("enabled");
		} else {
			this.getConfig().set("enabled", enabled);
		}
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		this.getConfig().set("enabled", enabled);
	    this.saveConfig();
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if(enabled && event.getPlayer().hasPermission("smitecaps.smite") && event.getMessage().toUpperCase().equals(event.getMessage())) {
			this.getServer().dispatchCommand(event.getPlayer(), "/smite");
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("smitecaps") && args.length > 0) {
			switch(args[0]) {
				case "toggle":
					enabled = !this.getConfig().getBoolean("enabled");
					this.getConfig().set("enabled", enabled);
					this.saveConfig();
					sender.sendMessage("Smiting " + (enabled ? "enabled" : "disabled"));
					break;
				case "on":
				case "enabled":
				case "true":
					enabled = true;
					this.getConfig().set("enabled", enabled);
					this.saveConfig();
					sender.sendMessage("Smiting enabled.");
					break;
				case "off":
				case "disabled":
				case "false":
					enabled = false;
					this.getConfig().set("enabled", enabled);
					this.saveConfig();
					sender.sendMessage("Smiting disabled.");
					break;
				case "status":
					sender.sendMessage("Smiting Admins on caps is currently " + enabled);
					break;
				default:
					return false;
			}
			return true;
		}
		return false;
	}
}