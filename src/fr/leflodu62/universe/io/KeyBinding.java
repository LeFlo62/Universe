package fr.leflodu62.universe.io;

import org.lwjgl.glfw.GLFW;

public class KeyBinding {

	public static final int SCROLL_UP = -64, SCROLL_DOWN = -63;
	
	private String translationKey;
	private KeyType type;
	private int key;
	private String categoryKey;
	private int mods;
	
	private boolean pressed;
	
	public KeyBinding(String translationKey, KeyType type, int key, String categoryKey) {
		this(translationKey, type, key, 0, categoryKey);
	}
	
	public KeyBinding(String translationKey, KeyType type, int key, int mods, String categoryKey) {
		this.translationKey = translationKey;
		this.type = type;
		this.key = key;
		this.mods = mods;
		this.categoryKey = categoryKey;
		
		if(key == GLFW.GLFW_KEY_LEFT_SHIFT) {
			this.mods += GLFW.GLFW_MOD_SHIFT;
		}
		else if(key == GLFW.GLFW_KEY_LEFT_ALT) {
			this.mods += GLFW.GLFW_MOD_ALT;
		}
		else if(key == GLFW.GLFW_KEY_LEFT_CONTROL) {
			this.mods += GLFW.GLFW_MOD_CONTROL;
		}
	}
	
	
	public void remap(int key) {
		this.remap(key, 0);
	}
	
	public void remap(int key, int mods) {
		this.key = key;
		this.mods = mods;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
	public boolean isPressed() {
		return pressed;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public KeyType getType() {
		return type;
	}

	public int getKey() {
		return key;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public int getMods() {
		return mods;
	}


	public static enum KeyType{
		KEYBOARD,
		MOUSE,
		GAMEPAD;
	}
	
}
