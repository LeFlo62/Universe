package fr.leflodu62.universe.render.shader;

public interface Shader {

	public void bindAttributes();
	
	public void getAllUniformLocations();
	
	public void start();
	
	public void stop();
	
	public void clean();
	
}
