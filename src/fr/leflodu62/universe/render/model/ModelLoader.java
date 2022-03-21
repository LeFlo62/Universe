package fr.leflodu62.universe.render.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ModelLoader {

	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();

	public RawModel loadTexturedModelToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadColoredModelToVAO(float[] positions, float[] colors, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 3, colors);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions, int coordSize) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, coordSize, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/coordSize);
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
	}

	private void storeDataInAttributeList(int attributeList, int coordSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeList, coordSize, GL20.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}


	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	/**
	 * Deletes created VAOs and VBOs
	 */
	public void clean() {
		for (int i = 0; i < vaos.size(); i++) {
			GL30.glDeleteVertexArrays(vaos.get(i));
		}
		for (int i = 0; i < vbos.size(); i++) {
			GL15.glDeleteBuffers(vbos.get(i));
		}
	}

}
