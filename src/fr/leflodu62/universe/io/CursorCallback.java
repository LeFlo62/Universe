package fr.leflodu62.universe.io;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import fr.leflodu62.universe.Tickable;

public class CursorCallback implements Tickable, GLFWCursorPosCallbackI {

	private static CursorCallback instance;
	
	private double lastX;
	private double lastY;
	
	private double deltaX;
	private double deltaY;

	public CursorCallback() {
		instance = this;
	}
	
	@Override
	public void invoke(long window, double xpos, double ypos) {
		deltaX = xpos - lastX;
		deltaY = ypos - lastY;
		
		lastX = xpos;
		lastY = ypos;
	}
	
	@Override
	public void tick() {
		deltaX = deltaY = 0;
	}
	
	/**
	 * @return the difference between the last movement in the X screenspace coordinates
	 */
	public double getDeltaX() {
		return deltaX;
	}
	
	/**
	 * @return the difference between the last movement in the Y screenspace coordinates
	 */
	public double getDeltaY() {
		return deltaY;
	}
	
	public static CursorCallback get() {
		return instance;
	}

}
