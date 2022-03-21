#version 400 core

in vec3 worldPos;

out vec4 out_Color;

void main(void){
	float stepSize = 1;
	float chunkSize = 32;

	float grad = 0.01;
	float grad2 = 0.001;

	vec4 col1 = vec4(0, 1, 0, 1);
	vec4 col2 = vec4(1, 0, 0, 1);
	vec4 col3 = vec4(0, 0.2, 0, 0.1);

	vec2 coord = worldPos.xz / stepSize; //square size in world space
	vec2 frac = fract(coord); //fractional parts of squares
	//interpolation, grad is smoothness of line gradients
	vec2 mult = smoothstep(0.0, grad, frac) - smoothstep(1.0-grad, 1.0, frac);

	vec2 frac2 = fract(worldPos.xz / chunkSize);
	vec2 mult2 = smoothstep(0.0, grad2, frac2) - smoothstep(1.0-grad2, 1.0, frac2);

	vec4 result = mix(col2, mix(col1, col3, mult.x * mult.y), mult2.x * mult2.y);

	out_Color = result;
}