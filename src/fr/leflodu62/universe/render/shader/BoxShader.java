package fr.leflodu62.universe.render.shader;

import org.joml.Matrix4f;

public class BoxShader extends GeometryShaderProgram implements ProjectionShader {

	private static final String GEOMETRY_FILE = "box.gsh";
	private static final String VERTEX_FILE = "box.vsh";
	private static final String FRAGMENT_FILE = "box.fsh";
	
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	
	public BoxShader() {
		super(GEOMETRY_FILE, VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	public void getAllUniformLocations() {
		projectionMatrixLocation = getUniformLocation("projectionMatrix");
		viewMatrixLocation = getUniformLocation("viewMatrix");
	}
	
	@Override
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadUniformMatrix4f(projectionMatrixLocation, matrix);
	}
	
	public void loadViewMatrix(Matrix4f matrix) {
		loadUniformMatrix4f(viewMatrixLocation, matrix);
	}

}
