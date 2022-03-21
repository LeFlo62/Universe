package fr.leflodu62.universe.objects;

import org.joml.Vector3f;

import fr.leflodu62.universe.render.model.ComplexModel;

/**
 * A visual world object.
 * @author LeFlo
 *
 */
public class VisibleObject extends GameObject {

	private ComplexModel model;

	public VisibleObject(ComplexModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(position, rotX, rotY, rotZ, scale);
		this.model = model;
	}

	public ComplexModel getModel() {
		return model;
	}

	public void setModel(ComplexModel model) {
		this.model = model;
	}

}
