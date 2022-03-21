package fr.leflodu62.universe.objects;

import org.joml.Vector3f;

/**
 * Something that can be located in the world.
 * @author LeFlo
 *
 */
public class GameObject {
	
	protected Vector3f position;
	protected float pitch, yaw, roll;
	protected float scale;
	
	public GameObject(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.position = position;
		this.pitch = rotX;
		this.yaw = rotY;
		this.roll = rotZ;
		this.scale = scale;
	}

	public void move(float dx, float dy, float dz) {
		position.add(dx, dy, dz);
	}
	
	public void rotate(float dx, float dy, float dz) {
		this.pitch += dx;
		this.yaw += dy;
		this.roll += dz;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
