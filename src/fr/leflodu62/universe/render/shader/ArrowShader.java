package fr.leflodu62.universe.render.shader;

import org.joml.Matrix4f;

public class ArrowShader extends ShaderProgram implements ProjectionShader {
	
	private static final String VERTEX_FILE = "arrow.vsh";
	private static final String FRAGMENT_FILE = "arrow.fsh";
	
	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	
	private int colorLocation;
	
	public ArrowShader() {
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
		
		colorLocation = getUniformLocation("color");
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
	
	public void loadColor(float x, float y, float z, float a) {
		loadUniformVector4f(colorLocation, x, y, z, a);
	}

}
