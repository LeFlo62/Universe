package fr.leflodu62.universe.render.shader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import fr.leflodu62.universe.Universe;

public abstract class GeometryShaderProgram implements Shader {
	
	private int programID;
	private int geometryShaderID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public GeometryShaderProgram(String geometryFile, String vertexFile, String fragmentFile) {
		geometryShaderID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, geometryShaderID);
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	@Override
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	@Override
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	@Override
	public void clean() {
		GL20.glDetachShader(programID, geometryShaderID);
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(geometryShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected final void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected void loadUniformFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadUniformVector3f(int location, Vector3f value) {
		loadUniformVector3f(location, value.x, value.y, value.z);
	}
	
	protected void loadUniformVector3f(int location, float x, float y, float z) {
		GL20.glUniform3f(location, x, y, z);
	}
	
	protected void loadUniformVector4f(int location, Vector4f value) {
		loadUniformVector4f(location, value.x, value.y, value.z, value.w);
	}
	
	protected void loadUniformVector4f(int location, float x, float y, float z, float w) {
		GL20.glUniform4f(location, x, y, z, w);
	}
	
	protected void loadUniformBoolean(int location, boolean value) {
		loadUniformFloat(location, value ? 1 : 0);
	}
	
	protected void loadUniformMatrix4f(int location, Matrix4f value) {
		value.get(matrixBuffer);
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	public static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Universe.class.getResourceAsStream("/assets/shaders/" + file)));
			String line;
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append(System.lineSeparator());
			}
			reader.close();
		} catch (Exception e) {
			System.err.println("An error occured while reading a shader file !");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("An error occured while compiling a shader (" + file +") !");
			System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.exit(-1);
		}
		return shaderID;
	}

}
