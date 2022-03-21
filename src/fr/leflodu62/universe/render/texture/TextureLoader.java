package fr.leflodu62.universe.render.texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class TextureLoader {
	
	private List<Integer> textures = new ArrayList<>();

	public static Texture DEFAULT_TEXTURE;
	
	public TextureLoader() {
		DEFAULT_TEXTURE = createTexture(2, 2, new float[] {1,0,1,1, 0,0,0,1, 0,0,0,1, 1,0,1,1});
	}
	
	public Texture loadTexture(String path) {
		int textureID;
		int width, height;
		ByteBuffer image;

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			image = STBImage.stbi_load("resources/assets/textures/" + path, w, h, comp, 4);
			if (image == null) {
				System.out.println("Failed to load texture file: " + "assets/textures/" + path + "\n" + STBImage.stbi_failure_reason());
				System.err.println(STBImage.stbi_failure_reason());
				 return DEFAULT_TEXTURE;
			}
			width = w.get();
			height = h.get();
		}

		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		textures.add(textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);
		
		return new Texture(textureID);
	}
	
	public Texture createTexture(int width, int height, float[] colors) {
		int textureID;
		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		textures.add(textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST); // sets MINIFICATION filtering to nearest
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // sets MAGNIFICATION filtering to nearest
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_FLOAT, BufferUtils.createFloatBuffer(colors.length).put(colors).flip());
		return new Texture(textureID);
	}
	
	public Texture createTexture(float r, float g, float b, float a) {
		int textureID;
		int width = 1, height = 1;
		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		textures.add(textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST); // sets MINIFICATION filtering to nearest
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // sets MAGNIFICATION filtering to nearest
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_FLOAT, BufferUtils.createFloatBuffer(4).put(new float[] {r, g, b, a}).flip());
		return new Texture(textureID);
	}
	
	public void clean() {
		for (int i = 0; i < textures.size(); i++) {
			GL11.glDeleteTextures(textures.get(i));
		}
	}

}
