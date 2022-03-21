package fr.leflodu62.universe.render.model;

import de.javagl.obj.Mtl;
import fr.leflodu62.universe.render.texture.Texture;

public class TexturedModel {
	
	private RawModel model;
	private Texture texture;
	private Mtl material;

	public TexturedModel(RawModel model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public TexturedModel(RawModel model, Texture texture, Mtl material) {
		this.model = model;
		this.texture = texture;
		this.material = material;
	}

	public RawModel getRawModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public TexturedModel setTexture(Texture texture) {
		this.texture = texture;
		return this;
	}

	public Mtl getMaterial() {
		return material;
	}

	public TexturedModel setMaterial(Mtl material) {
		this.material = material;
		return this;
	}
	

}
