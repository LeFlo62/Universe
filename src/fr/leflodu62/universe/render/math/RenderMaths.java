package fr.leflodu62.universe.render.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import fr.leflodu62.universe.objects.Camera;

public class RenderMaths {
	
	private static final Vector3f X = new Vector3f(1,0,0);
	private static final Vector3f Y = new Vector3f(0,1,0);
	private static final Vector3f Z = new Vector3f(0,0,1);
	
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		return new Matrix4f().identity().translate(translation).rotate((float)Math.toRadians(rx), X)
				.rotate((float)Math.toRadians(ry), Y)
				.rotate((float)Math.toRadians(rz), Z)
				.scale(scale);
	}

	public static Matrix4f createProjectionMatrix(float aspectRatio, float fov, float nearPlane, float farPlane) {
		float yScale = (float) ((1f/Math.tan(Math.toRadians(fov /2f))));
		float xScale = yScale / aspectRatio;
		float frustumLength = farPlane - nearPlane;
		return new Matrix4f().m00(xScale).m11(yScale).m22(-((farPlane + nearPlane)/frustumLength)).m23(-1f).m32(-((2*nearPlane*farPlane)/frustumLength)).m33(0);
	}

	public static Matrix4f createViewMatrix(Camera camera) {
		return new Matrix4f().identity().rotate((float)Math.toRadians(camera.getPitch()), X).rotate((float)Math.toRadians(camera.getYaw()), Y)
				.rotate((float)Math.toRadians(camera.getRoll()), Z).translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
	}

}
