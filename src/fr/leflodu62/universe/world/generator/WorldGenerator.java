package fr.leflodu62.universe.world.generator;

import java.util.Random;

import fr.leflodu62.universe.world.chunk.Chunk;

public interface WorldGenerator {
	
	public void generate(Chunk chunk, Random random);

}
