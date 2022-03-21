package fr.leflodu62.universe.render.shader;

import org.joml.Matrix4f;

public interface ProjectionShader extends Shader {
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix);

}
