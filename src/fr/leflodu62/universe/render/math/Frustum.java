package fr.leflodu62.universe.render.math;

import org.joml.Vector3f;

import fr.leflodu62.universe.objects.Camera;

public class Frustum {

	private float nh, nw;
	
	private static final Vector3f UP = new Vector3f(0,1,0);
	
	private Plane near, far, top, bottom, right, left;
	private Plane[] planes;

	public Vector3f Z;
	public Vector3f X;
	public Vector3f Y;

	public Frustum(Plane near, Plane far, Plane top, Plane bottom, Plane right, Plane left) {
		this.near = near;
		this.far = far;
		this.top = top;
		this.bottom = bottom;
		this.right = right;
		this.left = left;
		this.planes = new Plane[]{near, far, top, bottom, right, left};
	}
	
	/**
	 * Updates constants used in {@link Frustum#update(Camera)}
	 * This need to be called whenever the window is resized.
	 * 
	 * @param camera The camera object
	 * @param aspectRatio The window aspect ration
	 */
	public void resize(Camera camera, float aspectRatio) {
		float tang = (float) Math.tan(Math.toRadians(camera.getFov()) * 0.5);
		nh = camera.getNearPlane() * tang;
		nw = nh * aspectRatio;
	}
	
	/**
	 * Updates the frustum to the camera view.
	 * 
	 * @param camera The camera object
	 */
	public void update(Camera camera) {
		Vector3f position = camera.getPosition();
		Z = new Vector3f(camera.getDirection()).mul(-1).normalize();
		
		X = UP.cross(Z, new Vector3f()).normalize();
		
		Y = Z.cross(X, new Vector3f()).normalize();
		
		Vector3f nc = Z.mul(camera.getNearPlane(), new Vector3f()).mul(-1).add(position);
		
		Vector3f fc = Z.mul(camera.getFarPlane(), new Vector3f()).mul(-1).add(position);
		
		near.setNormal(Z.mul(-1, new Vector3f())).setPoint(nc);
		far.setNormal(new Vector3f(Z)).setPoint(fc);
		
		top.setNormal(Y.mul(nh, new Vector3f()).add(nc).sub(position).normalize().cross(X)).setPoint(Y.mul(nh, new Vector3f()).add(nc));
		bottom.setNormal(Y.mul(nh, new Vector3f()).mul(-1).add(nc).sub(position).normalize().cross(X).mul(-1)).setPoint(Y.mul(nh, new Vector3f()).mul(-1).add(nc));
		left.setNormal(X.mul(nw, new Vector3f()).mul(-1).add(nc).sub(position).normalize().cross(Y)).setPoint(X.mul(nw, new Vector3f()).mul(-1).add(nc));
		right.setNormal(X.mul(nw, new Vector3f()).add(nc).sub(position).normalize().cross(Y).mul(-1)).setPoint(X.mul(nw, new Vector3f()).add(nc));
	}

	/**
	 * Checks whether or not the specified cube is inside the frustum.
	 * This is Axis Aligned and returns true even if a portion of the
	 * cube is inside.
	 * 
	 * @param xmin
	 * @param ymin
	 * @param zmin
	 * @param xmax
	 * @param ymax
	 * @param zmax
	 * @return true if the specified object is inside the frustum.
	 */
	public boolean isInside(float xmin, float ymin, float zmin, float xmax, float ymax, float zmax) {
		for(Plane plane : planes) {
			Vector3f p = new Vector3f(xmin, ymin, zmin);
			if(plane.getNormal().x > 0) p.x = xmax;
			if(plane.getNormal().y > 0) p.y = ymax;
			if(plane.getNormal().z > 0) p.z = zmax;
			
			if(plane.distance(p) < 0) return false;
		}
		
		return true;
	}
	
	/**
	 * Creates an frustum with all planes set to the origin point and a null normal.
	 * 
	 * @return a Frustum with all planes set to the origin point and a null normal.
	 */
	public static Frustum emptyFrustum() {
		return new Frustum(new Plane(new Vector3f(0,0,0), new Vector3f(0,0,0)),
				new Plane(new Vector3f(0,0,0), new Vector3f(0,0,0)),
				new Plane(new Vector3f(0,0,0), new Vector3f(0,0,0)),
				new Plane(new Vector3f(0,0,0), new Vector3f(0,0,0)),
				new Plane(new Vector3f(0,0,0), new Vector3f(0,0,0)),
				new Plane(new Vector3f(0,0,0), new Vector3f(0,0,0)));
	}
}
