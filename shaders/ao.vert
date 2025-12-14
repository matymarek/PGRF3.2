#version 330

in vec2 inPosition;
out vec2 texCoord;

void main() {
    texCoord = inPosition;
    gl_Position = vec4(inPosition * 2 - 1, 0, 1);
}
