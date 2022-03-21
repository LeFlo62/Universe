package fr.leflodu62.universe.io;

import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;

import fr.leflodu62.universe.io.KeyBinding.KeyType;;

public class KeyBindings {
	
	public static KeyBinding WALK_FORWARD = new  KeyBinding("key.walk_forward", KeyType.KEYBOARD, GLFW.GLFW_KEY_W, "category.movement");
	public static KeyBinding WALK_LEFT = new  KeyBinding("key.walk_left", KeyType.KEYBOARD, GLFW.GLFW_KEY_A, "category.movement");
	public static KeyBinding WALK_BACKWARD = new  KeyBinding("key.walk_backward", KeyType.KEYBOARD, GLFW.GLFW_KEY_S, "category.movement");
	public static KeyBinding WALK_RIGHT = new  KeyBinding("key.walk_right", KeyType.KEYBOARD, GLFW.GLFW_KEY_D, "category.movement");
	public static KeyBinding UP = new  KeyBinding("key.walk_right", KeyType.KEYBOARD, GLFW.GLFW_KEY_SPACE, "category.movement");
	public static KeyBinding DOWN = new  KeyBinding("key.walk_right", KeyType.KEYBOARD, GLFW.GLFW_KEY_LEFT_SHIFT, "category.movement");
	
	public static KeyBinding WIREFRAME = new  KeyBinding("key.wireframe", KeyType.KEYBOARD, GLFW.GLFW_KEY_Z, "category.debug");
	public static KeyBinding GRID = new  KeyBinding("key.grid", KeyType.KEYBOARD, GLFW.GLFW_KEY_G, "category.debug");
	
	//TODO testing
	public static KeyBinding WHEEL_UP = new KeyBinding("key.test.wheel_up", KeyType.MOUSE, KeyBinding.SCROLL_UP, "category.test");
	public static KeyBinding WHEEL_DOWN = new KeyBinding("key.test.wheel_up", KeyType.MOUSE, KeyBinding.SCROLL_DOWN, "category.test");
	public static KeyBinding LEFT_CLICK = new KeyBinding("key.test.left_click", KeyType.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT, "category.test");
	public static KeyBinding RELEASE_MOUSE = new KeyBinding("key.test.release_mouse", KeyType.KEYBOARD, GLFW.GLFW_KEY_T, "category.test");
	public static KeyBinding MOVE_LIGHT = new KeyBinding("key.test.move_light", KeyType.KEYBOARD, GLFW.GLFW_KEY_L, "category.test");
	public static KeyBinding NORMALS = new KeyBinding("key.test.normals", KeyType.KEYBOARD, GLFW.GLFW_KEY_N, "category.test");
	public static KeyBinding BOXES = new KeyBinding("key.test.boxes", KeyType.KEYBOARD, GLFW.GLFW_KEY_B, "category.test");

	public KeyBindings() {
		KeyBindingRegistry registry = KeyBindingRegistry.get();
		
		try {
			for(Field field : getClass().getFields()) {
				if(field.getType().equals(KeyBinding.class)) {
					registry.registerKeyBinding((KeyBinding) field.get(null));
				}
			}
		} catch (Exception e) {
			System.err.println("An error occured while registering KeyBindings !");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
