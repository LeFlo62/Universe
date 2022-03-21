package fr.leflodu62.universe.objects;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Arrow extends VisibleObject {

	private float r;
	private float g;
	private float b;
	private boolean normal;

	public Arrow(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(null, position, rotX, rotY, rotZ, scale);
	}

	public Arrow setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		return this;
	}
	
	public float getR() {
		return r;
	}
	
	public float getG() {
		return g;
	}
	
	public float getB() {
		return b;
	}
	
	public static Arrow createFromTo(float x1, float y1, float z1, float x2, float y2, float z2) {
		return createFromTo(new Vector3f(x1,y1,z1), new Vector3f(x2,y2,z2).sub(x1, y1, z1));
	}
	
	public static Arrow createFromTo(Vector3f origin, Vector3f direction) {
		Quaternionf q = new Quaternionf();
		new Vector3f(1,0,0).rotationTo(direction, q);
		
		Vector3f rot = new Vector3f();
		q.getEulerAnglesXYZ(rot);
		
		Arrow arrow = new Arrow(origin, (float)Math.toDegrees(rot.x), (float)Math.toDegrees(rot.y), (float)Math.toDegrees(rot.z), direction.length());
		return arrow;
	}

	public Arrow setNormal(boolean normal) {
		this.normal = normal;
		return this;
	}
	
	public boolean isNormal() {
		return normal;
	}
	
}
