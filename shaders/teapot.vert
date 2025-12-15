#version 330
in vec3 inPosition;
in vec3 inNormal;
in vec2 inTexCoord;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;

out vec2 texCoord;
out vec3 normalVector;
out vec3 worldPos;

void main() {
    vec4 wp = uModel * vec4(inPosition, 1.0);
    worldPos = wp.xyz;

    mat3 nMat = mat3(uModel);
    normalVector = normalize(nMat * inNormal);

    texCoord = inTexCoord;

    gl_Position = uProj * uView * wp;
}
