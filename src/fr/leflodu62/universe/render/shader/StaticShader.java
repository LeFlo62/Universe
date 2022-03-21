package fr.leflodu62.universe.render.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import fr.leflodu62.universe.objects.Light;

public class StaticShader extends ShaderProgram implements ProjectionShader {

	private static final String VERTEX_FILE = "static.vsh";
	private static final String FRAGMENT_FILE = "static.fsh";
	
	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	
	private int lightPositionLocation;
	private int lightColorLocation;
	
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int useColorLocation;
	private int colorLocation;
	
	private int skyColorLocation;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
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
		
		useColorLocation = getUniformLocation("useColor");
		colorLocation = getUniformLocation("color");
		
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

	public void loadColor(float x, float y, float z, float a) {
		loadUniformBoolean(useColorLocation, true);
		loadUniformVector4f(colorLocation, x, y, z, a);
	}

	public void useTexture() {
		loadUniformBoolean(useColorLocation, false);
	}

	public void loadSkyColor(Vector3f color) {
		loadUniformVector3f(skyColorLocation, color);
	}
}
