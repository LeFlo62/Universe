package fr.leflodu62.universe.render.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import de.javagl.obj.Mtl;
import de.javagl.obj.Mtls;
import fr.leflodu62.universe.objects.Arrow;
import fr.leflodu62.universe.objects.Camera;
import fr.leflodu62.universe.objects.Light;
import fr.leflodu62.universe.objects.VisibleObject;
import fr.leflodu62.universe.render.WindowManager;
import fr.leflodu62.universe.render.math.Frustum;
import fr.leflodu62.universe.render.math.RenderMaths;
import fr.leflodu62.universe.render.model.ModelLoader;
import fr.leflodu62.universe.render.model.ObjLoader;
import fr.leflodu62.universe.render.model.RawModel;
import fr.leflodu62.universe.render.model.TexturedModel;
import fr.leflodu62.universe.render.shader.ArrowShader;
import fr.leflodu62.universe.render.shader.BoxShader;
import fr.leflodu62.universe.render.shader.GridShader;
import fr.leflodu62.universe.render.shader.ProjectionShader;
import fr.leflodu62.universe.render.shader.StaticShader;
import fr.leflodu62.universe.render.shader.WorldShader;
import fr.leflodu62.universe.world.World;
import fr.leflodu62.universe.world.chunk.Chunk;

public class MasterRenderer {
	
	public static Mtl DEFAULT_MATERIAL;
	
	private WindowManager windowManager;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Frustum cullingFrustum;
	
	private StaticShader staticShader = new StaticShader();
	private WorldShader worldShader = new WorldShader();
	private GridShader gridShader = new GridShader();
	private ArrowShader arrowShader = new ArrowShader();
	private BoxShader boxShader = new BoxShader();
	private ProjectionShader[] projectionShaders = {staticShader, worldShader, gridShader, arrowShader, boxShader};
	
	private ObjectRenderer objectRenderer;
	private WorldRenderer worldRenderer;
	private ArrowRenderer arrowRenderer;
	
	private List<Arrow> arrows = new ArrayList<>();
	private List<Chunk> chunks = new ArrayList<>();
	private Map<TexturedModel, List<VisibleObject>> objects = new HashMap<>();
	
	public MasterRenderer(WindowManager windowManager, Camera camera, ObjLoader objLoader, ModelLoader modelLoader) {
		this.windowManager = windowManager;
		
		DEFAULT_MATERIAL = Mtls.create("default");
		
		cullingFrustum = Frustum.emptyFrustum();
		cullingFrustum.resize(camera, windowManager.getAspectRatio());
		
		objectRenderer = new ObjectRenderer(staticShader);
		worldRenderer = new WorldRenderer(worldShader, modelLoader);
		arrowRenderer = new ArrowRenderer(arrowShader, objLoader.loadObj("arrow.obj"));
		createProjectionMatrix(windowManager.getAspectRatio(), camera.getFov(), camera.getNearPlane(), camera.getFarPlane());
	}
	
	public void createProjectionMatrix(float aspectRatio, float fov, float nearPlane, float farPlane) {
		projectionMatrix = RenderMaths.createProjectionMatrix(aspectRatio, fov, nearPlane, farPlane);
		for(ProjectionShader projShader : projectionShaders ) {
			projShader.start();
			projShader.loadProjectionMatrix(projectionMatrix);
			projShader.stop();
		}
	}
	
	public void prepare(World world, Camera camera) {
		if(windowManager.shouldResize()) {
			windowManager.aknowledgeResize();
			cullingFrustum.resize(camera, windowManager.getAspectRatio());
			createProjectionMatrix(windowManager.getAspectRatio(), camera.getFov(), camera.getNearPlane(), camera.getFarPlane());
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(world.getSkyColor().x, world.getSkyColor().y, world.getSkyColor().z, 1);
		
		this.viewMatrix = RenderMaths.createViewMatrix(camera);
		this.cullingFrustum.update(camera);
		objectRenderer.prepare();
		worldRenderer.prepare();
		arrowRenderer.prepare();
	}
	
	public void render(World world, Light sun) {
		staticShader.start();
		staticShader.loadSkyColor(world.getSkyColor());
		staticShader.loadLight(sun);
		staticShader.loadViewMatrix(viewMatrix);
		objectRenderer.renderObjects(objects);
		staticShader.stop();
		objects.clear();
		
		worldShader.start();
		worldShader.loadSkyColor(world.getSkyColor());
		worldShader.loadLight(sun);
		worldShader.loadViewMatrix(viewMatrix);
		worldRenderer.renderChunks(chunks);
		worldShader.stop();
		chunks.clear();
		
		arrowShader.start();
		arrowShader.loadViewMatrix(viewMatrix);
		arrowRenderer.renderArrows(arrows);
		arrowShader.stop();
		arrows.clear();
	}
	
	public void renderGrid(RawModel model) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		gridShader.start();
		gridShader.loadViewMatrix(viewMatrix);
		objectRenderer.renderGrid(model);
		gridShader.stop();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	public void renderBox(RawModel model) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		boxShader.start();
		boxShader.loadViewMatrix(viewMatrix);
		objectRenderer.renderBox(model);
		boxShader.stop();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	public void processObject(VisibleObject... visibleObjects) {
		for(VisibleObject object : visibleObjects) {
			for(TexturedModel model : object.getModel().getParts()) {
				if(!objects.containsKey(model)) {
					objects.put(model, new ArrayList<>());
				}
				objects.get(model).add(object);
			}
		}
	}
	
	public void processChunks(Chunk... chunks) {
		for(Chunk chunk : chunks) {
			if(chunk != null) {
				this.chunks.add(chunk);
			}
		}
	}
	
	public void processArrows(Arrow... arrows) {
		for(Arrow arrow : arrows) {
			if(arrow != null) {
				this.arrows.add(arrow);
			}
		}
	}
	
	public void clean() {
		staticShader.clean();
		gridShader.clean();
		worldShader.clean();
		arrowShader.clean();
	}
	
	public Frustum getCullingFrustum() {
		return cullingFrustum;
	}
	
}
