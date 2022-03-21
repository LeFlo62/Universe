package fr.leflodu62.universe.world.generator;

import java.util.Random;

import fr.leflodu62.universe.world.biome.Biome;
import fr.leflodu62.universe.world.chunk.Chunk;
import fr.leflodu62.universe.world.chunk.ChunkPos;
import fr.leflodu62.universe.world.generator.biome.BiomeGenerator;
import fr.leflodu62.universe.world.generator.biome.DefaultBiomeGenerator;
import fr.leflodu62.universe.world.generator.chunk.NoiseChunkGenerator;

public class DefaultWorldGenerator implements WorldGenerator {
	
	private NoiseChunkGenerator noiseChunkGenerator;
	private BiomeGenerator biomeGenerator;
	
	public DefaultWorldGenerator() {
		this.noiseChunkGenerator = new NoiseChunkGenerator();
		this.biomeGenerator = new DefaultBiomeGenerator();
	}

	@Override
	public void generate(Chunk chunk, Random random) {
		ChunkPos pos = chunk.getPos();
		
		float[][][] elevation = noiseChunkGenerator.generate(pos.getX(), pos.getY(), pos.getZ(), random);
		Biome[][] biomes = biomeGenerator.generate(elevation, pos.getX(), pos.getY(), pos.getY(), random);
		
		chunk.setElevation(elevation);
		chunk.setBiomes(biomes);
	}

}
