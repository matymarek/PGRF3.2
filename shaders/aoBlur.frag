#version 330

in vec2 texCoord;

uniform sampler2D aoTex;
uniform vec2 uResolution;

out vec4 outColor;

void main()
{
    vec2 texel = 1.0 / uResolution;

    vec3 sum = vec3(0.0);

    for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
            vec2 offset = vec2(x, y) * texel;
            sum += texture(aoTex, texCoord + offset).rgb;
        }
    }

    vec3 blurred = sum / 9.0;
    outColor = vec4(blurred, 1.0);
}
