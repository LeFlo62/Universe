package fr.leflodu62.universe.world.chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import fr.leflodu62.universe.Tickable;
import fr.leflodu62.universe.objects.Camera;
import fr.leflodu62.universe.utils.ArrayUtils;
import fr.leflodu62.universe.world.World;

public class ChunkManager implements Tickable {
	
	private Camera camera;
	private World world;
	
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(32);
	private int radius = 6;
	// Z X Y
	private ChunkPos[][][] relativeChunks = new ChunkPos[(radius << 1)+1][(radius << 1)+1][(radius << 1)+1];
	private ChunkPos cameraChunkPos;
	private ConcurrentLinkedDeque<ChunkPos> toUnloadChunks = new ConcurrentLinkedDeque<>();
	private ConcurrentLinkedDeque<ChunkPos> toLoadChunks = new ConcurrentLinkedDeque<>();
	private List<Chunk> chunksToRender = Collections.synchronizedList(new ArrayList<Chunk>());
	private ChunkPos currentCameraChunkPos;

	public ChunkManager(Camera camera, World world) {
		this.camera = camera;
		this.world = world;
		
		currentCameraChunkPos = new ChunkPos((int) camera.getPosition().x >> 5, (int) camera.getPosition().y >> 5, (int) camera.getPosition().z >> 5);
		cameraChunkPos = currentCameraChunkPos;
		for(int z = -radius; z <= radius; z++) {
			for(int x = -radius; x <= radius; x++) {
				for(int y = -radius; y <= radius; y++) {
					ChunkPos pos = new ChunkPos(currentCameraChunkPos.getX() + x, currentCameraChunkPos.getY() + y, currentCameraChunkPos.getZ() + z);
					relativeChunks[z + radius][x + radius][y + radius] = pos;
					toLoadChunks.add(pos);
				}
			}
		}
		int size = toLoadChunks.size();
		while(size > 0) {
			size -= radius << 1;
			executor.execute(() -> {
				for(int i = 0; i <= radius << 1 && !toLoadChunks.isEmpty(); i++) {
					chunksToRender.add(world.loadChunk(toLoadChunks.poll(), true));
				}
			});
		}
	}
	
	@Override
	public void tick() {
		try {
			currentCameraChunkPos = new ChunkPos((int) camera.getPosition().x >> 5, (int) camera.getPosition().y >> 5, (int) camera.getPosition().z >> 5);
			if(currentCameraChunkPos.getX() != cameraChunkPos.getX() || currentCameraChunkPos.getY() != cameraChunkPos.getY() || currentCameraChunkPos.getZ() != cameraChunkPos.getZ()) {
				int dx = currentCameraChunkPos.getX() - cameraChunkPos.getX();
				int dy = currentCameraChunkPos.getY() - cameraChunkPos.getY();
				int dz = currentCameraChunkPos.getZ() - cameraChunkPos.getZ();

				cameraChunkPos = currentCameraChunkPos;
				
				// Move array to be centered on camera pos
				if(dz > 0) {
					for(int x = -radius; x <= radius; x++) {
						for(int y = -radius; y <= radius; y++) {
							toUnloadChunks.add(relativeChunks[0][x + radius][y + radius]);
						}
					}
					ArrayUtils.shiftLeft(relativeChunks);
					for(int x = -radius; x <= radius; x++) {
						for(int y = -radius; y <= radius; y++) {
							ChunkPos pos = new ChunkPos(currentCameraChunkPos.getX() + x, currentCameraChunkPos.getY() + y, currentCameraChunkPos.getZ() + radius);
							relativeChunks[(radius << 1)][x + radius][y + radius] = pos;
							toLoadChunks.add(pos);
						}
					}
				} else if(dz < 0) {
					for(int x = -radius; x <= radius; x++) {
						for(int y = -radius; y <= radius; y++) {
							toUnloadChunks.add(relativeChunks[(radius << 1)][x + radius][y + radius]);
						}
					}
					ArrayUtils.shiftRight(relativeChunks);
					for(int x = -radius; x <= radius; x++) {
						for(int y = -radius; y <= radius; y++) {
							ChunkPos pos = new ChunkPos(currentCameraChunkPos.getX() + x, currentCameraChunkPos.getY() + y, currentCameraChunkPos.getZ() - radius);
							relativeChunks[0][x + radius][y + radius] = pos;
							toLoadChunks.add(pos);
						}
					}
				}

				if(dx > 0) {
					for(int z = -radius; z <= radius; z++) {
						for(int y = -radius; y <= radius; y++) {
							toUnloadChunks.add(relativeChunks[z + radius][0][y + radius]);
						}
						ArrayUtils.shiftLeft(relativeChunks[z + radius]);
						for(int y = -radius; y < radius; y++) {
							ChunkPos pos = new ChunkPos(currentCameraChunkPos.getX() + radius, currentCameraChunkPos.getY() + y, currentCameraChunkPos.getZ() + z);
							relativeChunks[z + radius][(radius << 1)][y + radius] = pos;
							toLoadChunks.add(pos);
						}
					}
				} else if(dx < 0) {
					for(int z = -radius; z <= radius; z++) {
						for(int y = -radius; y <= radius; y++) {
							toUnloadChunks.add(relativeChunks[z + radius][(radius << 1)][y + radius]);
						}
						ArrayUtils.shiftRight(relativeChunks[z + radius]);
						for(int y = -radius; y < radius; y++) {
							ChunkPos pos = new ChunkPos(currentCameraChunkPos.getX() - radius, currentCameraChunkPos.getY() + y, currentCameraChunkPos.getZ() + z);
							relativeChunks[z + radius][0][y + radius] = pos;
							toLoadChunks.add(pos);
						}
					}
				}

				if(dy > 0) {
					for(int z = -radius; z <= radius; z++) {
						for(int x = -radius; x <= radius; x++) {
							toUnloadChunks.add(relativeChunks[z + radius][x + radius][0]);
							ArrayUtils.shiftLeft(relativeChunks[z + radius][x + radius]);
							ChunkPos pos = new ChunkPos(currentCameraChunkPos.getX() + x, currentCameraChunkPos.getY() + radius, currentCameraChunkPos.getZ() + z);
							relativeChunks[z + radius][x + radius][(radius << 1)] = pos;
							toLoadChunks.add(pos);
						}
					}
				} else if(dy < 0) {
					for(int z = -radius; z <= radius; z++) {
						for(int x = -radius; x <= radius; x++) {
							toUnloadChunks.add(relativeChunks[z + radius][x + radius][(radius << 1)]);
							ArrayUtils.shiftRight(relativeChunks[z + radius][x + radius]);
							ChunkPos pos = new ChunkPos(currentCameraChunkPos.getX() + x, currentCameraChunkPos.getY() - radius, currentCameraChunkPos.getZ() + z);
							relativeChunks[z + radius][x + radius][0] = pos;
							toLoadChunks.add(pos);
						}
					}
				}
				
				int toLoadSize = toLoadChunks.size();
				while(toLoadSize > 0) {
					toLoadSize -= radius << 1;
					executor.execute(() -> {
						for(int i = 0; i < radius << 1; i++) {
							ChunkPos pos = toLoadChunks.poll();
							if(pos == null) continue;
							Chunk chunk = world.loadChunk(pos, true);
							chunksToRender.add(chunk);
						}
					});
				}

				int unLoadSize = toUnloadChunks.size();
				while(unLoadSize > 0) {
					unLoadSize -= radius << 1;
					executor.execute(() -> {
						for(int i = 0; i < radius << 1; i++) {
							ChunkPos pos = toUnloadChunks.poll();
							if(pos == null) continue;
							Chunk chunk = world.getChunk(pos, false);
							world.unloadChunk(chunk);
							chunksToRender.remove(chunk);
						}
					});
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		executor.shutdown();
	}
	
	public List<Chunk> getChunksToRender() {
		return chunksToRender;
	}

}
