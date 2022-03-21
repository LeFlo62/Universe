#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPosition;

out vec2 pass_texCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

out float visibility;

const float density = 0.005;
const float gradient = 2.2;

void main(void){
	vec4 worldPos = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCamera = viewMatrix * worldPos;

	gl_Position = projectionMatrix * positionRelativeToCamera;
	pass_texCoords = texCoords;

	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	toLightVector = lightPosition - worldPos.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPos.xyz;

	float distanceToCamera = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow(distanceToCamera*density, gradient));
	visibility = clamp(visibility, 0, 1);
}