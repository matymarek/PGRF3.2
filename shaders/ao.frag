#version 330

in vec2 texCoord;

uniform sampler2D depthTex;
uniform vec2 uResolution;
uniform float uRadius;
uniform float uBias;
uniform float uIntensity;

out vec4 outColor;

void main() {
    vec2 uv = vec2(1.0 - texCoord.x, 1.0 - texCoord.y);
    float depth = texture(depthTex, uv).r;
    if (depth >= 1.0) {
        outColor = vec4(1.0);
        return;
    }

    vec2 texelSize = 1.0 / uResolution;

    vec2 offsets[8] = vec2[](
    vec2( 1,  0),
    vec2(-1,  0),
    vec2( 0,  1),
    vec2( 0, -1),
    vec2( 1,  1),
    vec2(-1,  1),
    vec2( 1, -1),
    vec2(-1, -1)
    );

    float occlusion = 0.0;
    int samples = 8;

    for (int i = 0; i < samples; i++) {
        vec2 sampleUv = texCoord + offsets[i] * texelSize * uRadius;
        float sampleDepth = texture(depthTex, sampleUv).r;

        float rangeCheck = depth - sampleDepth;
        if (rangeCheck > uBias) {
            occlusion += 1.0;
        }
    }

    occlusion /= float(samples);
    float ao = 1.0 - occlusion;
    ao = pow(ao, 0.6);
    ao = clamp(ao, 0.4, 1.0);

    outColor = vec4(vec3(ao), 1.0);
}
