package fr.leflodu62.universe;

import org.lwjgl.opengl.GL11;

import fr.leflodu62.universe.io.KeyBindings;

public class Debug {

	public static boolean wireframe;
	public static boolean grid;
	public static boolean renderNormals;
	public static boolean renderBoxes;

	public static void tick() {
		if(KeyBindings.WIREFRAME.isPressed()) {
			if(wireframe) {
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			} else {
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			}
			KeyBindings.WIREFRAME.setPressed(false);
			wireframe = !wireframe;
		}

		if(KeyBindings.GRID.isPressed()) {
			KeyBindings.GRID.setPressed(false);
			grid = !grid;
		}
		if(KeyBindings.NORMALS.isPressed()) {
			KeyBindings.NORMALS.setPressed(false);
			renderNormals = !renderNormals;
		}
		if(KeyBindings.BOXES.isPressed()) {
			KeyBindings.BOXES.setPressed(false);
			renderBoxes = !renderBoxes;
		}
	}

}
