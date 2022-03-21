package fr.leflodu62.universe.world.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import fr.leflodu62.universe.world.World;

public class ChunkProvider {
	
	private ConcurrentHashMap<ChunkPos, Chunk> pos2Chunk = new ConcurrentHashMap<>();
	private List<Chunk> loadedChunks = new ArrayList<>(144);
	private Set<ChunkPos> droppedChunks = ConcurrentHashMap.newKeySet();
	
	private World world;
	
	public ChunkProvider(World world) {
		this.world = world;
	}
	
	public Chunk loadChunk(ChunkPos pos, boolean keep) {
		droppedChunks.remove(pos);
		Chunk chunk = pos2Chunk.get(pos);
		if(chunk == null) {
			chunk = loadChunkFromFile(pos);
			if(chunk == null) {
				chunk = new Chunk(world, pos);
				if(world.getWorldGenerator() != null) {
					world.getWorldGenerator().generate(chunk, world.getRandom());
				}
			}
			
			pos2Chunk.put(pos, chunk);
		}
		if(keep && !loadedChunks.contains(chunk)) loadedChunks.add(chunk);
		
		return chunk;
	}
	
	public Chunk provideChunk(ChunkPos pos, boolean keep) {
		Chunk chunk = pos2Chunk.get(pos);
		return chunk != null ? chunk : this.loadChunk(pos, keep);
	}
	
	public void dropChunk(Chunk chunk) {
		this.dropChunk(chunk.getPos());
	}
	
	public void dropChunk(ChunkPos pos) {
		this.droppedChunks.add(pos);
	}
	
	public void unloadDroppedChunks() {
		this.droppedChunks.forEach(pos -> {
			Chunk c = pos2Chunk.get(pos);
			if(c != null) {
				loadedChunks.remove(c);
				pos2Chunk.remove(pos);
			}
		});
		droppedChunks.clear();
	}
	
	//TODO save chunks to files
	public void saveChunk(Chunk chunk) {
		
	}
	
	public Chunk loadChunkFromFile(ChunkPos pos) {
		return null;
	}
	
	public List<Chunk> getLoadedChunks() {
		return loadedChunks;
	}

	public boolean isChunkLoaded(ChunkPos pos) {
		return pos2Chunk.contains(pos);
	}

}
