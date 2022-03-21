package fr.leflodu62.universe.world.generator.biome;

import java.util.Random;

import fr.leflodu62.universe.world.biome.Biome;

public interface BiomeGenerator {
	
	/**
	 * Creates a 3D array of size Chunk.SIZE+1. The +1 is to connect each chunks between them.
	 * 
	 * @param chunkX
	 * @param chunkY
	 * @param chunkZ
	 * @param random
	 * @return vertex values array
	 */
	public Biome[][] generate(float[][][] elevation, int chunkX, int chunkY, int chunkZ, Random random);

}
