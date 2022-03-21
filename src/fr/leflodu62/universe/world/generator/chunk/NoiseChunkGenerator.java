package fr.leflodu62.universe.world.generator.chunk;

import java.util.Random;

import fr.leflodu62.universe.world.SimplexNoise;
import fr.leflodu62.universe.world.chunk.Chunk;

public class NoiseChunkGenerator implements ChunkGenerator {

	private SimplexNoise surfaceNoise;
	private SimplexNoise hangNoise;
	
	private float scale = 16f;

	public NoiseChunkGenerator() {
		int octaves = 6;
		double[] amplitudes = new double[octaves];
		double[] frequencies = new double[octaves];
		
		for(int i = 0; i < octaves; i++) {
			frequencies[i] = Math.pow(2, i);
			amplitudes[i] = 1/frequencies[i];
		}
		
		this.surfaceNoise = new SimplexNoise(octaves, frequencies, amplitudes, 1);
	}

	// https://www.youtube.com/watch?v=9STUhFb7DtI&t=317s
	@Override
	public float[][][] generate(int chunkX, int chunkY, int chunkZ, Random random) {
		float[][][] values = new float[Chunk.SIZE + 1][Chunk.SIZE + 1][Chunk.SIZE + 1];
		for(int z = 0; z <= Chunk.SIZE; z++) {
			for(int x = 0; x <= Chunk.SIZE; x++) {
				for(int y = 0; y <= Chunk.SIZE; y++) {
					float v2d = (float) (surfaceNoise.getNoise2((double) (chunkX*Chunk.SIZE + x)/(Chunk.SIZE*scale), (double) (chunkZ*Chunk.SIZE + z)/(Chunk.SIZE*scale)));
					values[x][y][z] = -chunkY*Chunk.SIZE - y + ((float)Math.pow(v2d*1.2, 8))*172;
				}
			}
		}
		return values;
	}

}
