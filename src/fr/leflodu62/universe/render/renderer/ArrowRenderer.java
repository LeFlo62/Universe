package fr.leflodu62.universe.render.renderer;

import java.util.List;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import fr.leflodu62.universe.objects.Arrow;
import fr.leflodu62.universe.render.math.RenderMaths;
import fr.leflodu62.universe.render.model.ComplexModel;
import fr.leflodu62.universe.render.model.RawModel;
import fr.leflodu62.universe.render.model.TexturedModel;
import fr.leflodu62.universe.render.shader.ArrowShader;
import fr.leflodu62.universe.render.shader.GridShader;

public class ArrowRenderer {

	private ArrowShader arrowShader;
	private ComplexModel complexModel;

	public ArrowRenderer(ArrowShader arrowShader, ComplexModel complexModel) {
		this.arrowShader = arrowShader;
		this.complexModel = complexModel;
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void renderArrows(List<Arrow> arrows) {
		for(TexturedModel model : complexModel.getParts()) {
			prepareTexturedModel(model);
			for(Arrow arrow : arrows) {
				prepareInstance(arrow);
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
	}
	
	private void prepareInstance(Arrow arrow) {
						arrowShader.loadColor(arrow.getR(), arrow.getG(), arrow.getB(), 1);
						
						Matrix4f transformationMatrix = RenderMaths.createTransformationMatrix(arrow.getPosition(), arrow.getPitch(), arrow.getYaw(), arrow.getRoll(), arrow.getScale());
						arrowShader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void unbindTexturedModel() {
					GL20.glDisableVertexAttribArray(2);
				GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public void renderGrid(RawModel model, GridShader gridShader) {
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
	
}
