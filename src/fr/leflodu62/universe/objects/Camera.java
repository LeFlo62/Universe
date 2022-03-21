package fr.leflodu62.universe.objects;

import org.joml.Vector3f;

import fr.leflodu62.universe.Tickable;
import fr.leflodu62.universe.Universe;
import fr.leflodu62.universe.io.CursorCallback;
import fr.leflodu62.universe.io.KeyBindings;
import fr.leflodu62.universe.world.World;

/**
 * The camera moving around the world.
 * @author LeFlo
 *
 */
public class Camera extends GameObject implements Tickable {

	private Vector3f forward = new Vector3f(0,0,-1);
	private Vector3f direction = new Vector3f(0,0,-1);
	
	private float fov = 80f;
	private float nearPlane = 0.05f;
	private float farPlane = 1000f;
	
	private float speed = 0.5f;
	
	public Camera() {
		this(new Vector3f(0,0,0), 0, 0, 0, 1.0f);
	}
	
	public Camera(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(position, rotX, rotY, rotZ, scale);
	}

	@Override
	public void tick() {
		float cp = (float)Math.cos(Math.toRadians(pitch+180));
		float sp = (float)Math.sin(Math.toRadians(pitch+180));
		
		float sy = (float)Math.sin(Math.toRadians(yaw+90));
		float cy = (float)Math.cos(Math.toRadians(yaw+90));
				
		forward = new Vector3f(-cy, 0, -sy).normalize().mul(speed);
		direction = new Vector3f(cp * cy, sp, sy * cp).normalize();
				
		if(KeyBindings.WALK_FORWARD.isPressed()) {
			move(forward.x , 0, forward.z);
		}
		if(KeyBindings.WALK_LEFT.isPressed()) {
			move(forward.z, 0, -forward.x);
		}
		if(KeyBindings.WALK_BACKWARD.isPressed()) {
			move(-forward.x , 0, -forward.z);
		}
		if(KeyBindings.WALK_RIGHT.isPressed()) {
			move(-forward.z, 0, forward.x);
		}
		if(KeyBindings.UP.isPressed()) {
			move(0, speed, 0);
		}
		if(KeyBindings.DOWN.isPressed()) {
			move(0, -speed, 0);
		}
		if(KeyBindings.WHEEL_UP.isPressed()) {
			KeyBindings.WHEEL_UP.setPressed(false);
			if(speed <= 4) speed += 0.2f;
		}
		if(KeyBindings.WHEEL_DOWN.isPressed()) {
			KeyBindings.WHEEL_DOWN.setPressed(false);
			if(speed >= 0.1f) speed -= 0.2f;
		}
		if(Universe.get().getWindowManager().isCursorCatched()) {
			float drx = (float)CursorCallback.get().getDeltaY()*0.1f;
			float dry = (float)CursorCallback.get().getDeltaX()*0.1f;
			if(drx + pitch > 90) {
				pitch = 90;
				drx = 0;
			}
			if(drx + pitch < -90) {
				pitch = -90;
				drx = 0;
			}
			if(dry + yaw > 180) {
				yaw = -180 -dry;
			}
			if(dry + yaw < -180) {
				yaw = 180 +dry;
			}
			rotate(drx, dry, 0);
		}
	}
	
	public Vector3f getForward() {
		return forward;
	}
	
	public Vector3f getDirection() {
		return direction;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}

	public float getNearPlane() {
		return nearPlane;
	}

	public void setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
	}

	public float getFarPlane() {
		return farPlane;
	}

	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
	}

}
