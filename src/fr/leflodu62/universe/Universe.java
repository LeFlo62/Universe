package fr.leflodu62.universe;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import de.javagl.obj.Mtl;
import de.javagl.obj.Mtls;
import fr.leflodu62.universe.io.CursorCallback;
import fr.leflodu62.universe.io.KeyBindingRegistry;
import fr.leflodu62.universe.io.KeyBindings;
import fr.leflodu62.universe.objects.Arrow;
import fr.leflodu62.universe.objects.Camera;
import fr.leflodu62.universe.objects.Light;
import fr.leflodu62.universe.objects.VisibleObject;
import fr.leflodu62.universe.render.WindowManager;
import fr.leflodu62.universe.render.model.ComplexModel;
import fr.leflodu62.universe.render.model.ModelData;
import fr.leflodu62.universe.render.model.ModelLoader;
import fr.leflodu62.universe.render.model.OBJFileLoader;
import fr.leflodu62.universe.render.model.ObjLoader;
import fr.leflodu62.universe.render.model.RawModel;
import fr.leflodu62.universe.render.model.TexturedModel;
import fr.leflodu62.universe.render.renderer.MasterRenderer;
import fr.leflodu62.universe.render.texture.TextureLoader;
import fr.leflodu62.universe.world.World;
import fr.leflodu62.universe.world.chunk.Chunk;
import fr.leflodu62.universe.world.chunk.ChunkPos;
import fr.leflodu62.universe.world.generator.DefaultWorldGenerator;

public class Universe {

	// TODO https://learnopengl.com/In-Practice/Text-Rendering
	// https://github.com/mlomb/freetype-jni#Face

	private static Universe instance;

	public static void main(String[] args) {
		new Universe();
	}

	private WindowManager windowManager;

	private MasterRenderer masterRenderer;

	private Camera camera;

	private TextureLoader textureLoader;
	private ModelLoader modelLoader;

	// TODO testing
	private Arrow xArrow = new Arrow(new Vector3f(0, 0, 0), 0, 0, 0, 1).setColor(1, 0, 0);
	private Arrow yArrow = new Arrow(new Vector3f(0, 0, 0), 0, 0, 90, 1).setColor(0, 1, 0);
	private Arrow zArrow = new Arrow(new Vector3f(0, 0, 0), 0, -90, 0, 1).setColor(0, 0, 1);

	private RawModel importedBoxModel;
	private VisibleObject imported;
	private VisibleObject dragon;
	private Light light;

	private World world;

	private RawModel gridModel;

	private List<Arrow> arrows = new ArrayList<>();

	public Universe() {
		instance = this;
		windowManager = new WindowManager();

		camera = new Camera();
		camera.setPosition(new Vector3f(Chunk.SIZE/2, 60, Chunk.SIZE/2));
//		camera.setPosition(new Vector3f(-1027, 5, 559));

		modelLoader = new ModelLoader();
		textureLoader = new TextureLoader();
		ObjLoader objLoader = new ObjLoader(modelLoader, textureLoader);
		masterRenderer = new MasterRenderer(windowManager, camera, objLoader, modelLoader);

		new KeyBindings();

		// TODO testing
		float gridRadius = 1000;
		gridModel = modelLoader.loadTexturedModelToVAO(new float[] { -gridRadius, 0, -gridRadius, -gridRadius, 0, gridRadius, gridRadius, 0, gridRadius, gridRadius, 0, -gridRadius },
				new float[] { 0, 0, 0, 1, 1, 1, 1, 0 }, new float[] { 0, 1, 0, 0, 1, 0 }, new int[] { 0, 1, 3, 3, 1, 2 });

		ModelData dragonData = OBJFileLoader.loadOBJ("dragon");
		RawModel dragonModel = modelLoader.loadTexturedModelToVAO(dragonData.getVertices(), dragonData.getTextureCoords(), dragonData.getNormals(), dragonData.getIndices());
		Mtl dragonMaterial = Mtls.create("dragon");
		dragonMaterial.setNs(0.8f);
		TexturedModel texturedDragonModel = new TexturedModel(dragonModel, textureLoader.createTexture(1, 1, 1, 1).setReflectivity(0.8f)).setMaterial(dragonMaterial);
		dragon = new VisibleObject(new ComplexModel(new TexturedModel[] { texturedDragonModel }), new Vector3f(0, 0, -5), 0, 0, 0, 1.0f);

		ComplexModel importedModel = objLoader.loadObj("Community_School.obj");
		this.imported = new VisibleObject(importedModel, new Vector3f(0, 0, -50), 0, 180, 0, 0.2f);
		this.importedBoxModel = modelLoader.loadToVAO(new float[] { importedModel.getSmallestPoint().x, importedModel.getSmallestPoint().y, importedModel.getSmallestPoint().z,
				importedModel.getSmallestPoint().x + importedModel.getSize().x, importedModel.getSmallestPoint().y + importedModel.getSize().y,
				importedModel.getSmallestPoint().z + importedModel.getSize().z }, 3);

		light = new Light(new Vector3f(camera.getPosition()).add(0, 500, 0), new Vector3f(1, 1, 1));

		world = new World(new DefaultWorldGenerator(), 0);

		// Origin
		arrows.add(xArrow);
		arrows.add(yArrow);
		arrows.add(zArrow);

		loop();
	}

	final float frameCap = 60.0F;
	final float tickCap = 20.0F;

	public void loop() {
		long lastTickTime = System.nanoTime();
		long lastRenderTime = System.nanoTime();

		double tickTime = 10E8/tickCap;
		double renderTime = 10E8/frameCap;

		int ticks = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();

		// init chunk array

		while(!GLFW.glfwWindowShouldClose(windowManager.getWindowHandle())) {
			if(System.nanoTime() - lastTickTime >= tickTime) {
				// update();

				world.tick();

				ticks++;
				lastTickTime += tickTime;
			} else if(System.nanoTime() - lastRenderTime >= renderTime) {
				dragon.rotate(0, 1, 0);

				masterRenderer.prepare(world, camera);
				if(Debug.grid)
					masterRenderer.renderGrid(gridModel);
//				masterRenderer.processObject(dragon);
				masterRenderer.processObject(imported);


				for(Arrow arrow : arrows) {
					if(Debug.renderNormals || !arrow.isNormal()) {
						masterRenderer.processArrows(arrow);
					}
				}
				
				if(Debug.renderBoxes) {
					System.out.println("renderBoxes");
					masterRenderer.renderBox(importedBoxModel);
				}

				List<Chunk> chunksToRender = world.getChunkManager().getChunksToRender();
				synchronized(chunksToRender) {
					Iterator<Chunk> chunks = chunksToRender.iterator();
					while(chunks.hasNext()) {
						Chunk chunk = chunks.next();
						if(chunk != null) {
							ChunkPos pos = chunk.getPos();
							if(masterRenderer.getCullingFrustum().isInside(pos.getX()*Chunk.SIZE, pos.getY()*Chunk.SIZE, pos.getZ()*Chunk.SIZE, pos.getX()*Chunk.SIZE + Chunk.SIZE,
									pos.getY()*Chunk.SIZE + Chunk.SIZE, pos.getZ()*Chunk.SIZE + Chunk.SIZE)) {
								masterRenderer.processChunks(chunk);
							}
						}
					}
				}

				masterRenderer.render(world, light);
				frames++;

				// Update inputs
				camera.tick();

				if(KeyBindings.MOVE_LIGHT.isPressed()) {
					KeyBindings.MOVE_LIGHT.setPressed(false);
					light.setPosition(new Vector3f(camera.getPosition()));
				}

				windowManager.tick();

				Debug.tick();

				KeyBindingRegistry.get().tick();
				CursorCallback.get().tick();

				GLFW.glfwSwapBuffers(windowManager.getWindowHandle());

				GLFW.glfwPollEvents();

				lastRenderTime += renderTime;
			} else {
				try {
					Thread.sleep(1);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
//				 System.out.println(frames + " fps, " + ticks + " ticks");
				ticks = 0;
				frames = 0;
			}

		}

		shutdown();
	}

	/**
	 * Kills the application.
	 * 
	 */
	public void shutdown() {
		world.close();
		modelLoader.clean();
		textureLoader.clean();
		masterRenderer.clean();
		windowManager.terminate();
		System.exit(0);
	}

	public void addArrow(Arrow arrow) {
		this.arrows.add(arrow);
	}

	public void removeArrow(Arrow arrow) {
		this.arrows.remove(arrow);
	}

	public static Universe get() {
		return instance;
	}

	public WindowManager getWindowManager() {
		return windowManager;
	}

	public Camera getCamera() {
		return camera;
	}

	public World getWorld() {
		return world;
	}

}
