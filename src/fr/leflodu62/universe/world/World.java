package fr.leflodu62.universe.world;

import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;

import fr.leflodu62.universe.Tickable;
import fr.leflodu62.universe.Universe;
import fr.leflodu62.universe.world.chunk.Chunk;
import fr.leflodu62.universe.world.chunk.ChunkManager;
import fr.leflodu62.universe.world.chunk.ChunkPos;
import fr.leflodu62.universe.world.chunk.ChunkProvider;
import fr.leflodu62.universe.world.generator.WorldGenerator;

public class World implements Tickable {
	
	public static final int WORLD_CHUNKS_RADIUS = 16;

	private ChunkProvider chunkProvider;
	private ChunkManager chunkManager;
	
	private WorldGenerator worldGenerator;

	private Random random;

	private Vector3f skyColor = new Vector3f(0.46f, 0.67f, 1);
	
	public World(WorldGenerator worldGenerator, long seed) {
		this.worldGenerator = worldGenerator;
		this.random = new Random(seed);
		this.chunkProvider = new ChunkProvider(this);
		this.chunkManager = new ChunkManager(Universe.get().getCamera(), this);
	}
	
	@Override
	public void tick() {
		this.chunkManager.tick();
	}
	
	public void close() {
		this.chunkManager.close();
	}
	
	public float getValue(int x, int y, int z, boolean keep) {
		return getChunk(new ChunkPos(x >> 5, y >> 5, z >> 5), keep).getValue((x & (Chunk.SIZE-1)), (y & (Chunk.SIZE-1)), (z & (Chunk.SIZE-1)));
	}
	
	public Chunk getChunk(ChunkPos pos, boolean keep) {
		return chunkProvider.provideChunk(pos, keep);
	}
	
	public Chunk loadChunk(ChunkPos pos, boolean keep) {
		return chunkProvider.loadChunk(pos, keep);
	}
	
	public synchronized void unloadChunk(Chunk chunk) {
		chunkProvider.dropChunk(chunk);
	}
	
	public void unloadChunk(ChunkPos pos) {
		chunkProvider.dropChunk(pos);
	}
	
	public List<Chunk> getLoadedChunks() {
		return chunkProvider.getLoadedChunks();
	}
	
	public ChunkProvider getChunkProvider() {
		return chunkProvider;
	}
	
	public ChunkManager getChunkManager() {
		return chunkManager;
	}
	
	public Random getRandom() {
		return random;
	}
	
	public WorldGenerator getWorldGenerator() {
		return worldGenerator;
	}
	
	public void unloadDroppedChunks() {
		chunkProvider.unloadDroppedChunks();
	}

	public boolean isChunkLoaded(ChunkPos pos) {
		return chunkProvider.isChunkLoaded(pos);
	}
	
	public Vector3f getSkyColor() {
		return skyColor;
	}
	
	public void setSkyColor(Vector3f skyColor) {
		this.skyColor = skyColor;
	}

}
