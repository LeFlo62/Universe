package fr.leflodu62.universe.render.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.javagl.obj.FloatTuple;
import fr.leflodu62.universe.render.MarchingCubes;
import fr.leflodu62.universe.render.math.RenderMaths;
import fr.leflodu62.universe.render.model.ModelLoader;
import fr.leflodu62.universe.render.model.RawModel;
import fr.leflodu62.universe.render.shader.WorldShader;
import fr.leflodu62.universe.utils.ColorBlender;
import fr.leflodu62.universe.world.chunk.Chunk;
import fr.leflodu62.universe.world.chunk.ChunkPos;

public class WorldRenderer {
	
	//TODO ne plus cache les differents models, plutot les VAOid, ou les stocker dans un objet a part.
	private Map<ChunkPos, RawModel> chunkModels = new HashMap<>();
	
	private WorldShader worldShader;
	
	private ModelLoader modelLoader;

	private float[] colorFractions = {0, 0.2f, 0.95f};
	private Vector3f[] colorsToBlend = {new Vector3f(0.1f, 0.7f, 0.3f), new Vector3f(0.51f, 0.51f, 0.51f), new Vector3f(0.9f, 0.9f, 0.9f)};

	public WorldRenderer(WorldShader worldShader, ModelLoader modelLoader) {
		this.worldShader = worldShader;
		this.modelLoader = modelLoader;
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void renderChunks(List<Chunk> chunks) {
		for(Chunk chunk : chunks) {
			RawModel model;
			if(!chunkModels.containsKey(chunk.getPos())) {
				model = generateModelMarchingCubes(chunk);
				chunkModels.put(chunk.getPos(), model);
			} else {
				model = chunkModels.get(chunk.getPos());
			}
			
			prepareTexturedModel(model);
			prepareInstance(chunk);
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}
	
	private RawModel generateModelMarchingCubes(Chunk chunk) {
		float surface = 0;
		
		float[][][] values = chunk.getElevation();
		
		List<FloatTuple> positions = new ArrayList<>();
		
		for(int y = 0; y < Chunk.SIZE; y++) {
			for (int z = 0; z < Chunk.SIZE; z++) {
				for (int x = 0; x < Chunk.SIZE; x++) {
					float[] cube = MarchingCubes.getCubeValues(x, y, z, values);
					
					int cubeIndex = 0;
					for(int i = 0; i < 8; i++) {
						if(cube[i] > surface) {
							cubeIndex |= 1 << i;
						}
					}
					
					int config = MarchingCubes.EDGE_TABLE[cubeIndex];
					if(config == 0) continue;
					
					FloatTuple[] cubeVertices = MarchingCubes.getCubeVertices(config, x, y, z, values, surface);
					
					int[] edges = MarchingCubes.TRIANGULATION_TABLE[cubeIndex];
					for (int i = 0; edges[i] != -1; i+=3) {
						for(int j = 0; j < 3; j++) {
							FloatTuple point = cubeVertices[edges[i+j]];
							positions.add(point);
						}
					}
				}
			}
		}
		
		float[] positionsArray = new float[positions.size()*3];
		float[] colors = new float[positions.size()*3];
		int[] indicesArray = new int[positions.size()];
		for(int i = 0; i < positions.size(); i++) {
			FloatTuple point = positions.get(i);
			positionsArray[i*3] = point.getX();
			positionsArray[i*3+1] = point.getY();
			positionsArray[i*3+2] = point.getZ();
			
			float progress = (float)(point.getY() + chunk.getPos().getY()*Chunk.SIZE )/200f;
			Vector3f color = ColorBlender.blendColors(colorFractions, colorsToBlend, progress);
			colors[i*3] = color.x;
			colors[i*3 + 1] = color.y;
			colors[i*3 + 2] = color.z;
			
			indicesArray[i] = i;
		}
		
		float[] normals = new float[positionsArray.length];
		for(int i = 0; i < positions.size(); i+=3) {
			FloatTuple p1 = positions.get(i);
			FloatTuple p2 = positions.get(i+1);
			FloatTuple p3 = positions.get(i+2);
			
			float vx = p2.getX() - p1.getX();
			float vy = p2.getY() - p1.getY();
			float vz = p2.getZ() - p1.getZ();
		
			float wx = p3.getX() - p1.getX();
			float wy = p3.getY() - p1.getY();
			float wz = p3.getZ() - p1.getZ();
			
			float x = vy*wz - vz*wy;
			float y = vz*wx - vx*wz;
			float z = vx*wy - vy*wx;
						
			float length = (float) x*x + y*y + z*z;
			
			float nx = x / length;
			float ny = y / length;
			float nz = z / length;
			
			normals[i*3] = nx;
			normals[i*3+1] = ny;
			normals[i*3+2] = nz;
			
			normals[i*3+3] = nx;
			normals[i*3+4] = ny;
			normals[i*3+5] = nz;
			
			normals[i*3+6] = nx;
			normals[i*3+7] = ny;
			normals[i*3+8] = nz;
		}
		
		return modelLoader.loadColoredModelToVAO(positionsArray, colors, normals, indicesArray);
	}
	
	public void prepareTexturedModel(RawModel rawModel) {
		GL30.glBindVertexArray(rawModel.getVaoID());
			GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
					GL20.glEnableVertexAttribArray(2);

						worldShader.loadShineVariables(1, 0);
	}
	
	private void prepareInstance(Chunk chunk) {
						Matrix4f transformationMatrix = RenderMaths.createTransformationMatrix(new Vector3f(chunk.getPos().getX()*Chunk.SIZE, chunk.getPos().getY()*Chunk.SIZE, chunk.getPos().getZ()*Chunk.SIZE), 0, 0, 0, 1);
						worldShader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void unbindTexturedModel() {
					GL20.glDisableVertexAttribArray(2);
				GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
