package fr.leflodu62.universe.world.generator.biome;

import java.util.Random;

import fr.leflodu62.universe.world.SimplexNoise;
import fr.leflodu62.universe.world.biome.Biome;
import fr.leflodu62.universe.world.biome.Biomes;
import fr.leflodu62.universe.world.chunk.Chunk;

public class DefaultBiomeGenerator implements BiomeGenerator {

	private SimplexNoise temperatureNoise;
	private SimplexNoise humidityNoise;
	private double temperatureScale = 90.2;
	private float maxTemperature = 50f;
	private double humidityScale = 140.3;

	public DefaultBiomeGenerator() {
		this.temperatureNoise = new SimplexNoise(4, 0.4, 1);
		this.humidityNoise = new SimplexNoise(4, 0.2, 2);
	}
	
	@Override
	public Biome[][] generate(float[][][] elevation, int chunkX, int chunkY, int chunkZ, Random random) {
		Biome[][] biomes = new Biome[Chunk.SIZE+1][Chunk.SIZE+1];
		for(int z = 0; z <= Chunk.SIZE; z++) {
			for(int x = 0; x <= Chunk.SIZE; x++) {
				double bx = x + Chunk.SIZE * chunkX;
				double bz = z + Chunk.SIZE * chunkZ;
				
				float temperature = (float) (temperatureNoise.getNoise2((double)(bx)/(Chunk.SIZE*temperatureScale), (double)bz/(Chunk.SIZE*temperatureScale))+1)/2;
				float humidity = (float) (humidityNoise.getNoise2((double)(bx)/(Chunk.SIZE*humidityScale ), (double)bz/(Chunk.SIZE*humidityScale))+1)/2;
				biomes[x][z] = Biomes.get(temperature*maxTemperature, humidity);
			}
		}
		return biomes;
	}

}
