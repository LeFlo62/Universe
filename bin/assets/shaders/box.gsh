#version 400 core

layout (lines) in;
layout (line_strip, max_vertices = 16) out;

void main(){
	vec4 nearest = gl_in[0].gl_Position;
	vec4 furthest = gl_in[1].gl_Position;

	gl_Position = nearest;
	EmitVertex();

	gl_Position = vec4(furthest.x,  nearest.yzw);
	EmitVertex();

	gl_Position = vec4(furthest.x, nearest.y, furthest.zw);
	EmitVertex();

	gl_Position = vec4(nearest.xy, furthest.zw);
	EmitVertex();

	gl_Position = nearest;
	EmitVertex();

	gl_Position = vec4(nearest.x, furthest.y, nearest.zw);
	EmitVertex();
	EndPrimitive();

	gl_Position = vec4(nearest.x, furthest.yzw);
	EmitVertex();

	gl_Position = vec4(nearest.x, furthest.y, nearest.zw);
	EmitVertex();

	gl_Position = vec4(furthest.xy,  nearest.zw);
	EmitVertex();

	gl_Position = furthest;
	EmitVertex();

	gl_Position = vec4(nearest.x, furthest.yzw);
	EmitVertex();

	gl_Position = vec4(nearest.xy, nearest.zw);
	EmitVertex();
	EndPrimitive();

	gl_Position = vec4(furthest.x, nearest.y, furthest.zw);
	EmitVertex();

	gl_Position = furthest;
	EmitVertex();
	EndPrimitive();

	gl_Position = vec4(furthest.x,  nearest.yzw);
	EmitVertex();

	gl_Position = vec4(furthest.xy,  nearest.zw);
	EmitVertex();
	EndPrimitive();
}