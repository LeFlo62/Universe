#version 400 core

in vec2 pass_texCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

in float visibility;

uniform sampler2D textureSampler;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

uniform float useColor;
uniform vec4 color;

uniform vec3 skyColor;

out vec4 out_Color;

void main(void){
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitToLightVector = normalize(toLightVector);

	float ndotl = dot(unitNormal, unitToLightVector);
	float brightness = max(ndotl, 0.0);
	vec3 diffuse = brightness * lightColor;

	vec3 unitToCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitToLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

	float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
	specularFactor = max(specularFactor, 0.0);
	float damperFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = damperFactor * reflectivity * lightColor;

	vec4 pixelColor;
	if(useColor == 1){
		pixelColor = color;
	} else {
		pixelColor = texture(textureSampler, pass_texCoords);
	}

	out_Color = vec4(diffuse, 1.0) * pixelColor + vec4(finalSpecular, 1.0);
	out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
}