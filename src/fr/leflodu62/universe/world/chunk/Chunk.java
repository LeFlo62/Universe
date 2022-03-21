package fr.leflodu62.universe.world.chunk;

import org.joml.Vector3f;

import fr.leflodu62.universe.world.World;
import fr.leflodu62.universe.world.biome.Biome;

public class Chunk {
	
	public static final int SIZE = 32;
	
	private World world;
	private ChunkPos pos;
	
	public Vector3f color = new Vector3f(0.1f, 0.7f, 0.3f);
	
	private Biome[][] biomes;
	private float[][][] elevation;
	
	public Chunk(World world, ChunkPos pos) {
		this.world = world;
		this.pos = pos;
	}

	public World getWorld() {
		return world;
	}
	
	public ChunkPos getPos() {
		return pos;
	}
	
	public float getValue(int x, int y, int z) {
		return elevation[x][y][z];
	}
	
	public void setElevation(float[][][] elevation) {
		this.elevation = elevation;
	}
	
	public float[][][] getElevation() {
		return elevation;
	}
	
	public void setBiomes(Biome[][] biomes) {
		this.biomes = biomes;
	}
	
	public Biome[][] getBiomes() {
		return biomes;
	}
}
