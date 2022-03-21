package fr.leflodu62.universe.render.texture;

public class Texture {
	
	private int textureID;
	
	private float reflectivity;
	
	public Texture(int textureID) {
		this.textureID = textureID;
	}

	public int getTextureID() {
		return textureID;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public Texture setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}
	
}
