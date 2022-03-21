package fr.leflodu62.universe.render.renderer;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.javagl.obj.Mtl;
import fr.leflodu62.universe.objects.VisibleObject;
import fr.leflodu62.universe.render.math.RenderMaths;
import fr.leflodu62.universe.render.model.RawModel;
import fr.leflodu62.universe.render.model.TexturedModel;
import fr.leflodu62.universe.render.shader.GridShader;
import fr.leflodu62.universe.render.shader.StaticShader;
import fr.leflodu62.universe.render.texture.Texture;
import fr.leflodu62.universe.render.texture.TextureLoader;

public class ObjectRenderer {

	private StaticShader staticShader;

	public ObjectRenderer(StaticShader staticShader) {
		this.staticShader = staticShader;
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void renderObjects(Map<TexturedModel, List<VisibleObject>> objects) {
		for(TexturedModel model : objects.keySet()) {
			prepareTexturedModel(model);
			List<VisibleObject> batch = objects.get(model);
			for(VisibleObject object : batch) {
				prepareInstance(object);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	public void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
			GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
					GL20.glEnableVertexAttribArray(2);

						Texture texture = model.getTexture();
						if(texture == null) {
							texture = TextureLoader.DEFAULT_TEXTURE;
						}
					
						Mtl material = model.getMaterial();
						if(material == null) {
							material = MasterRenderer.DEFAULT_MATERIAL;
						}
						
						staticShader.loadShineVariables(material.getNs(), texture.getReflectivity());
						
						if(material.getMapKd() == null) {
							staticShader.loadColor(material.getKd().getX(), material.getKd().getY(), material.getKd().getZ(), material.getD());
						} else {
							staticShader.useTexture();
							GL13.glActiveTexture(GL13.GL_TEXTURE0);
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
						}
						
	}
	
	private void prepareInstance(VisibleObject object) {
						Matrix4f transformationMatrix = RenderMaths.createTransformationMatrix(object.getPosition(), object.getPitch(), object.getYaw(), object.getRoll(), object.getScale());
						staticShader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void unbindTexturedModel() {
					GL20.glDisableVertexAttribArray(2);
				GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public void renderGrid(RawModel model) {
		GL30.glBindVertexArray(model.getVaoID());
		{
			GL20.glEnableVertexAttribArray(0);
			{
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			GL20.glDisableVertexAttribArray(0);
		}
		GL30.glBindVertexArray(0);
	}
	
	public void renderBox(RawModel model) {
		GL30.glBindVertexArray(model.getVaoID());
		{
			GL20.glEnableVertexAttribArray(0);
			{
				GL11.glDrawArrays(GL11.GL_LINE, 0, model.getVertexCount());
			}
			GL20.glDisableVertexAttribArray(0);
		}
		GL30.glBindVertexArray(0);
	}
	
}
