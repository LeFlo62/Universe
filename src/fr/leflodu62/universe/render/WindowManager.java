package fr.leflodu62.universe.render;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import fr.leflodu62.universe.io.CursorCallback;
import fr.leflodu62.universe.io.KeyBindingRegistry;
import fr.leflodu62.universe.io.KeyBindings;

public class WindowManager implements GLFWWindowSizeCallbackI {
	
	private static final String TITLE = "Universe";
	private long window;
	
	private int width = 1280, height = 720;
	
	private int targetedFPS = 64;
	private boolean resize;
	
	public WindowManager() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		
		window = GLFW.glfwCreateWindow(width, height, TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
		
		if(window == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			GLFW.glfwGetWindowSize(window, pWidth, pHeight);

			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1); //V-SYNC
		GLFW.glfwShowWindow(window);
		
		GL.createCapabilities();
		
		KeyBindingRegistry keyBindingRegistry = new KeyBindingRegistry();
		GLFW.glfwSetKeyCallback(window, keyBindingRegistry.getKeyboardCallback());
		GLFW.glfwSetMouseButtonCallback(window, keyBindingRegistry.getMouseButtonCallback());
		GLFW.glfwSetScrollCallback(window, keyBindingRegistry.getScrollCallback());
		GLFW.glfwSetCursorPosCallback(window, new CursorCallback());
		GLFW.glfwSetWindowSizeCallback(window, this);
	}
	
	public void toggleFullScreen() {
		if(GLFW.glfwGetWindowMonitor(window) == MemoryUtil.NULL) {
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, targetedFPS);
		} else {
			GLFW.glfwSetWindowMonitor(window, MemoryUtil.NULL, 0, 0, width, height, targetedFPS);
		}
	}

	/**
	 * Kills the window.
	 */
	public void terminate() {
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
	
	public long getWindowHandle() {
		return window;
	}

	public int getTargetedFPS() {
		return targetedFPS;
	}

	public void setTargetedFPS(int targetedFPS) {
		this.targetedFPS = targetedFPS;
	}

	public float getAspectRatio() {
		return (float)width / (float)height;
	}

	public boolean isCursorCatched() {
		return GLFW.glfwGetInputMode(window, GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED;
	}

	@Override
	public void invoke(long window, int width, int height) {
		this.width = width;
		this.height = height;
		this.resize = true;
		GL11.glViewport(0, 0, width, height);
	}

	public boolean shouldResize() {
		return resize;
	}

	public void aknowledgeResize() {
		this.resize = false;
	}

	public void tick() {
		if(KeyBindings.LEFT_CLICK.isPressed()) {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		}
		if(KeyBindings.RELEASE_MOUSE.isPressed()) {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
			IntBuffer width = BufferUtils.createIntBuffer(1);
			IntBuffer height = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowSize(window, width, height);
			GLFW.glfwSetCursorPos(window, width.get() / 2, height.get() / 2);
		}
	}

}
