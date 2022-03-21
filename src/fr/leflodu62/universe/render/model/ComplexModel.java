package fr.leflodu62.universe.render.model;

import org.joml.Vector3f;

public class ComplexModel {
	
	private TexturedModel[] parts;
	
	private Vector3f size;
	private Vector3f smallestPoint;

	public ComplexModel(TexturedModel[] parts) {
		this.setParts(parts);
	}

	public TexturedModel[] getParts() {
		return parts;
	}

	public void setParts(TexturedModel[] parts) {
		this.parts = parts;
	}

	public Vector3f getSize() {
		return size;
	}

	public void setSize(Vector3f size) {
		this.size = size;
	}
	
	public Vector3f getSmallestPoint() {
		return smallestPoint;
	}
	
	public void setSmallestPoint(Vector3f smallestPoint) {
		this.smallestPoint = smallestPoint;
	}

}
