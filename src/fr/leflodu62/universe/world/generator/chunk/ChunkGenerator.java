package fr.leflodu62.universe.world.generator.chunk;

import java.util.Random;

public interface ChunkGenerator {
	
	/**
	 * Creates a 3D array of size Chunk.SIZE+1. The +1 is to connect each chunks between them.
	 * @param biomes 
	 * 
	 * @param chunkX
	 * @param chunkY
	 * @param chunkZ
	 * @param random
	 * @return vertex values array
	 */
	public float[][][] generate(int chunkX, int chunkY, int chunkZ, Random random);

}
