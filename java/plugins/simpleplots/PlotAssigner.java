package nukeduck.simpleplots;

import java.util.HashMap;
import java.util.UUID;

import nukeduck.simpleplots.util.Vec2;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotAssigner {
	private HashMap<UUID, Vec2> plots = new HashMap<UUID, Vec2>();

	public Vec2 findNextAvailablePlot() {
		int x = 0, z = 0;
		boolean xPos, zPos;

		while((xPos = this.plots.containsKey(++x)) && (xPos = !this.plots.containsKey(-x)));
		while((zPos = this.plots.containsKey(++z)) && (zPos = !this.plots.containsKey(-z)));

		return new Vec2(xPos ? x : -x, zPos ? z : -z);
	}

	public void assignPlot(CommandSender sender, Player player, int x, int y) {assignPlot(sender, player, new Vec2(x, y));}
	public void assignPlot(CommandSender sender, Player player, Vec2 plot) {
		plots.put(player.getUniqueId(), plot);
	}
}
