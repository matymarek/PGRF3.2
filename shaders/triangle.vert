#version 330

in vec3 inPosition;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;
uniform vec3 uColor;

out vec3 color;

void main() {
    color = uColor;
    vec4 worldPos = uModel * vec4(inPosition, 1.0);
    gl_Position = uProj * uView * worldPos;
}
