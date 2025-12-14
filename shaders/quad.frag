#version 330

in vec2 texCoord;

uniform sampler2D textureScene;
uniform sampler2D textureAO;

out vec4 outColor;

void main() {
    vec3 baseColor = texture(textureScene, texCoord).rgb;
    float ao = texture(textureAO, texCoord).r;
    float aoStrength = 0.5;
    float aoFactor = mix(1.0, ao, aoStrength);
    vec3 finalColor = baseColor * aoFactor;

    outColor = vec4(finalColor, 1.0);
}
