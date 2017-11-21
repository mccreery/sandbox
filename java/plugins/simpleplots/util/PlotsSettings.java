package nukeduck.simpleplots.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PlotsSettings {
	private JavaPlugin plugin;

	public Vec2 plotSize;
	public int pathWidth;
	public int halfPath;
	public Material pathType;

	public PlotsSettings(JavaPlugin plugin) {
		this.plugin = plugin;
		this.load();
	}

	public void setPathWidth(int pathWidth) {
		this.pathWidth = (pathWidth / 2) * 2;
		this.halfPath = pathWidth / 2;
	}

	public void load() {
		FileConfiguration config = this.plugin.getConfig();
		this.plotSize = new Vec2(config.getInt("plotSize.width"), config.getInt("plotSize.length"));
		this.setPathWidth(config.getInt("pathWidth"));
		this.pathType = Material.getMaterial(config.getString("generator.path"));
	}

	public void save() {
		FileConfiguration config = this.plugin.getConfig();
		config.set("plotSize.width", this.plotSize.x);
		config.set("plotSize.length", this.plotSize.y);
		config.set("pathWidth", this.pathWidth);
		config.set("generator.path", this.pathType.toString());
		this.plugin.saveConfig();
	}
}
