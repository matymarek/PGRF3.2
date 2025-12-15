#version 330

in vec2 texCoord;
in vec3 normalVector;
in vec3 worldPos;

uniform sampler2D textureBricks;
uniform vec3 uLightPos;
uniform vec3 uViewPos;
uniform vec3 uLightDir;
uniform float uConstantAtt;
uniform float uLinearAtt;
uniform float uQuadraticAtt;
uniform int uEnableAmbient;
uniform int uEnableDiffuse;
uniform int uEnableSpecular;
uniform int uEnableSpot;
uniform int uEnableAttenuation;
uniform int uEnableWireframe;

const vec3 AMBIENT_COLOR  = vec3(0.1, 0.1, 0.1);
const vec3 DIFFUSE_COLOR  = vec3(0.9, 0.8, 0.8);
const vec3 SPECULAR_COLOR = vec3(1.0, 1.0, 1.0);
const float SHININESS = 16.0;

out vec4 outColor;

void main()
{
    if (uEnableWireframe == 1) {
        outColor = vec4(1.0, 0.0, 1.0, 1.0);
    }
    else {
        vec3 baseColor = texture(textureBricks, texCoord).rgb;

        // základní vektory
        vec3 N = normalize(normalVector);
        vec3 L = normalize(uLightPos - worldPos);
        vec3 V = normalize(uViewPos  - worldPos);
        vec3 H = normalize(L + V);

        float NdotL = max(dot(N, L), 0.0);
        float NdotH = max(dot(N, H), 0.0);

        vec3 ambient  = AMBIENT_COLOR * baseColor;
        vec3 diffuse  = DIFFUSE_COLOR * baseColor * NdotL;
        float specFactor = (NdotL > 0.0) ? pow(NdotH, SHININESS) : 0.0;
        vec3 specular = SPECULAR_COLOR * specFactor;

        float spot = 1.0;
        if (uEnableSpot == 1) {
            vec3 LtoFrag = normalize(worldPos - uLightPos);
            vec3 dir = normalize(uLightDir);
            float theta = dot(dir, LtoFrag);
            float spotCutOff = 0.985;
            spot = (theta > spotCutOff) ? 1.0 : 0.0;
        }
        float attenuation = 1.0;
        if (uEnableAttenuation == 1) {
            float dist = length(uLightPos - worldPos);
            attenuation = 1.0 / (uConstantAtt + uLinearAtt * dist + uQuadraticAtt * dist * dist);
        }
        float lightFactor = spot * attenuation;

        vec3 color = vec3(0);

        if (uEnableAmbient  == 1) color += ambient;
        if (uEnableDiffuse  == 1) color += diffuse * lightFactor;
        if (uEnableSpecular == 1) color += specular * lightFactor;

        outColor = vec4(color, 1.0);
    }
}
