package fr.leflodu62.universe.render.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import fr.leflodu62.universe.objects.Light;

public class WorldShader extends ShaderProgram implements ProjectionShader {
	
	private static final String VERTEX_FILE = "world.vsh";
	private static final String FRAGMENT_FILE = "world.fsh";
	
	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	
	private int lightPositionLocation;
	private int lightColorLocation;
	
	private int shineDamperLocation;
	private int reflectivityLocation;
	
	private int skyColorLocation;
	
	public WorldShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "color");
		bindAttribute(2, "normal");
	}

	@Override
	public void getAllUniformLocations() {
		transformationMatrixLocation = getUniformLocation("transformationMatrix");
		projectionMatrixLocation = getUniformLocation("projectionMatrix");
		viewMatrixLocation = getUniformLocation("viewMatrix");
		
		lightPositionLocation = getUniformLocation("lightPosition"); 
		lightColorLocation = getUniformLocation("lightColor"); 
		
		shineDamperLocation = getUniformLocation("shineDamper");
		reflectivityLocation = getUniformLocation("reflectivity");
		
		skyColorLocation = getUniformLocation("skyColor");
	}
	
	@Override
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadUniformMatrix4f(projectionMatrixLocation, matrix);
	}
	
	public void loadViewMatrix(Matrix4f matrix) {
		loadUniformMatrix4f(viewMatrixLocation, matrix);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadUniformMatrix4f(transformationMatrixLocation, matrix);
	}
	
	public void loadLight(Light light) {
		super.loadUniformVector3f(lightPositionLocation, light.getPosition());
		loadUniformVector3f(lightColorLocation, light.getColor());
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		loadUniformFloat(shineDamperLocation, damper);
		loadUniformFloat(reflectivityLocation, reflectivity);
	}
	
	public void loadSkyColor(Vector3f color) {
		loadUniformVector3f(skyColorLocation, color);
	}

}
