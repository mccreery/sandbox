package nukeduck.simpleplots;

import java.util.Random;

import nukeduck.simpleplots.util.Vec2;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class PlotGenerator extends ChunkGenerator {
	SimplePlots plugin;

	public PlotGenerator(SimplePlots plugin) {
		this.plugin = plugin;
	}

	private static final int CHUNK_WIDTH = 16;
	private static final int CHUNK_HEIGHT = 128;
	private static final int CHUNK_DEPTH = 16;

	private static final int CHUNK_SIZE = CHUNK_WIDTH * CHUNK_HEIGHT * CHUNK_DEPTH;

	@SuppressWarnings("deprecation")
	private void setBlock(byte[] chunk, int x, int y, int z, Material material) {
		chunk[(x * 16 + z) * 128 + y] = (byte) material.getId();
	}

	@Override
	public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
		Material path = this.plugin.settings.pathType;
		Material plot = Material.GRASS;
		Vec2 plotSize = this.plugin.settings.plotSize;
		int pathWidth = this.plugin.settings.pathWidth;
		int halfPath = this.plugin.settings.halfPath;

		int chunkPosX = chunkX * CHUNK_WIDTH;
		int chunkPosZ = chunkZ * CHUNK_DEPTH;

		byte[] blocks = new byte[CHUNK_SIZE];
		for(int x = 0; x < CHUNK_WIDTH; x++) {
			for(int z = 0; z < CHUNK_DEPTH; z++) {
				int i = chunkPosX + x - halfPath;
				int j = chunkPosZ + z - halfPath;

				Material material = i % plotSize.x < pathWidth || j % plotSize.y < pathWidth ?
					path : plot;
				setBlock(blocks, x, 0, z, material);
			}
		}
		return blocks;
	}
}
