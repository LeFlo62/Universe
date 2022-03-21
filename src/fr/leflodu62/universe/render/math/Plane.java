package fr.leflodu62.universe.render.math;

import org.joml.Vector3f;

public class Plane {

	private Vector3f point;
	private Vector3f normal;

	public Plane(Vector3f point, Vector3f normal) {
		this.point = point;
		this.normal = normal;
	}
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public Vector3f getPoint() {
		return point;
	}
	
	public Plane setNormal(Vector3f normal) {
		this.normal = normal;
		return this;
	}
	
	public Plane setPoint(Vector3f point) {
		this.point = point;
		return this;
	}
	
	public float distance(Vector3f point) {
		return normal.dot(new Vector3f(point).sub(this.point));
	}

}
