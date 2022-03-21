package fr.leflodu62.universe.io;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import fr.leflodu62.universe.Tickable;
import fr.leflodu62.universe.io.KeyBinding.KeyType;

public class KeyBindingRegistry implements Tickable {
	
	private List<KeyBinding> keybindings = new ArrayList<>();

	private KeyboardCallback keyboardCallback;
	private MouseButtonCallback mouseButtonCallback;
	private ScollCallback scrollCallback;
	
	private static KeyBindingRegistry instance;
	
	public KeyBindingRegistry() {
		instance = this;
		keyboardCallback = new KeyboardCallback();
		mouseButtonCallback = new MouseButtonCallback();
		scrollCallback = new ScollCallback();
	}
	
	public void registerKeyBinding(KeyBinding keybinding) {
		if(keybindings.contains(keybinding)) {
			throw new RuntimeException("A keybinding is being registered multiple times !");
		}
		keybindings.add(keybinding);
	}
	
	public void unregisterKeyBinding(KeyBinding keybinding) {
		keybindings.remove(keybinding);
	}
	
	@Override
	public void tick() {
		//keybindings.forEach(k -> k.setPressed(false));
	}

	public KeyboardCallback getKeyboardCallback() {
		return keyboardCallback;
	}
	
	public MouseButtonCallback getMouseButtonCallback() {
		return mouseButtonCallback;
	}
	
	public ScollCallback getScrollCallback() {
		return scrollCallback;
	}
	
	public static KeyBindingRegistry get() {
		return instance;
	}
	
	public class KeyboardCallback implements GLFWKeyCallbackI{
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			for(int i = 0; i < keybindings.size(); i++) {
				KeyBinding keybinding = keybindings.get(i);
				if(keybinding.getType().equals(KeyType.KEYBOARD)) {
					if(keybinding.getKey() == key) {
						if(action == GLFW.GLFW_REPEAT) continue;
						keybinding.setPressed((keybinding.getMods() == 0 || keybinding.getMods() == mods) && action != GLFW.GLFW_RELEASE);
					}
				}
			}
		}
	}
	
	public class MouseButtonCallback implements GLFWMouseButtonCallbackI{
		@Override
		public void invoke(long window, int key, int action, int mods) {
			for(int i = 0; i < keybindings.size(); i++) {
				KeyBinding keybinding = keybindings.get(i);
				if(keybinding.getType().equals(KeyType.MOUSE)) {
					if(keybinding.getKey() == key) {
						if(action == GLFW.GLFW_REPEAT) continue;
						keybinding.setPressed((keybinding.getMods() == 0 || keybinding.getMods() == mods) && action != GLFW.GLFW_RELEASE);
					}
				}
			}
		}
	}
	
	public class ScollCallback implements GLFWScrollCallbackI{
		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			for(int i = 0; i < keybindings.size(); i++) {
				KeyBinding keybinding = keybindings.get(i);
				if(keybinding.getType().equals(KeyType.MOUSE)) {
					if(keybinding.getKey() == KeyBinding.SCROLL_UP && yoffset > 0 || keybinding.getKey() == KeyBinding.SCROLL_DOWN && yoffset < 0) {
						keybinding.setPressed(true);
					}
				}
			}
		}
	}
	
}
