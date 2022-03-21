package fr.leflodu62.universe.render.shader;

import org.joml.Matrix4f;

public class GridShader extends ShaderProgram implements ProjectionShader {

	private static final String VERTEX_FILE = "grid.vsh";
	private static final String FRAGMENT_FILE = "grid.fsh";
	
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	
	public GridShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
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
