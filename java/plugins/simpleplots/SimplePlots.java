package nukeduck.simpleplots;

import java.util.HashMap;
import java.util.UUID;

import nukeduck.simpleplots.util.PlotsSettings;
import nukeduck.simpleplots.util.Vec2;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class SimplePlots extends JavaPlugin implements Listener {
	public PlotsSettings settings;
	HashMap<UUID, Vec2> plots = new HashMap<UUID, Vec2>();

	@Override
	public void onEnable() {
		this.settings = new PlotsSettings(this);
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new PlotGenerator(this);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(!(e.getBlock().getWorld().getGenerator() instanceof PlotGenerator)) return;
		UUID uuid = e.getPlayer().getUniqueId();
		if(!plots.containsKey(uuid)) return;

		if(!isInPlot(e.getBlock(), settings.plotSize, settings.halfPath, plots.get(uuid)))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(!(e.getBlock().getWorld().getGenerator() instanceof PlotGenerator)) return;
		UUID uuid = e.getPlayer().getUniqueId();
		if(!plots.containsKey(uuid)) return;

		if(!isInPlot(e.getBlock(), settings.plotSize, settings.halfPath, plots.get(uuid)))
			e.setCancelled(true);
	}

	public static boolean isInPlot(Block block, Vec2 plotSize, int halfPath, Vec2 plot) {
		int plotXReal = plot.x * plotSize.x;
		int plotZReal = plot.y * plotSize.y;
		int x = block.getX();
		int z = block.getZ();

		return x >= plotXReal + halfPath && x < plotXReal + plotSize.x - halfPath
				&& z >= plotZReal + halfPath && z < plotZReal + plotSize.y - halfPath;
	}
}
