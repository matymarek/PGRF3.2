#version 330

in vec2 inPosition;
uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;
uniform float uTime;
uniform int uSurfaceId;
uniform vec3 uLightPos;
out vec2 texCoord;
out vec3 lightVector;
out vec3 normalVector;
out vec3 worldPos;
const float PI = 3.14159265;

// --- Kartézské plochy ---

// f1(x,y) = 0.5 * cos(sqrt(20x^2 + 20y^2) + uTime)
vec3 cartesianSurface1(float u, float v) {
    float x = mix(-1.5, 1.5, u);
    float y = mix(-1.5, 1.5, v);
    float r2 = 20.0 * (x * x + y * y);
    float z = 0.5 * cos(sqrt(r2) + uTime);
    return vec3(x, z, y);
}

// f2 – zvlněná rovina
vec3 cartesianSurface2(float u, float v) {
    float x = mix(-2.0, 2.0, u);
    float y = mix(-2.0, 2.0, v);
    float z = 0.3 * sin(4.0 * x + uTime) * cos(4.0 * y);
    return vec3(x, z, y);
}

// --- Sférické plochy ---

// s1 – něco jako „ježatá koule“
vec3 sphericalSurface1(float u, float v) {
    float theta = mix(0.0, PI, v);        // 0..π
    float phi   = mix(0.0, 2.0*PI, u);    // 0..2π

    float r = 1.0 + 0.3 * cos(5.0 * phi + uTime);

    float x = r * sin(theta) * cos(phi);
    float y = r * cos(theta);
    float z = r * sin(theta) * sin(phi);
    return vec3(x, y, z);
}

// s2 – trochu jinak „šišatá“ koule
vec3 sphericalSurface2(float u, float v) {
    float theta = mix(0.0, PI, v);
    float phi   = mix(0.0, 2.0*PI, u);

    float r = 1.0 + 0.2 * sin(4.0 * theta + uTime) * sin(3.0 * phi);

    float x = r * sin(theta) * cos(phi);
    float y = r * cos(theta);
    float z = r * sin(theta) * sin(phi);
    return vec3(x, y, z);
}

// --- Cylindrické plochy ---

// c1 – zkroucený válec
vec3 cylindricalSurface1(float u, float v) {
    float phi = mix(0.0, 2.0*PI, u);
    float z   = mix(-1.5, 1.5, v);

    float r = 0.8 + 0.2 * sin(3.0 * z + uTime);

    float x = r * cos(phi);
    float y = r * sin(phi);
    return vec3(x, z, y);
}

// c2 – zvlněná trubka
vec3 cylindricalSurface2(float u, float v) {
    float phi = mix(0.0, 2.0*PI, u);
    float z   = mix(-1.5, 1.5, v);

    float r = 0.6 + 0.3 * sin(4.0 * phi) * cos(2.0 * z + 0.5 * uTime);

    float x = r * cos(phi);
    float y = r * sin(phi);
    return vec3(x, z, y);
}

vec3 surfacePosition(float u, float v) {
    if (uSurfaceId == 0) return cartesianSurface1(u, v);
    if (uSurfaceId == 1) return cartesianSurface2(u, v);
    if (uSurfaceId == 2) return sphericalSurface1(u, v);
    if (uSurfaceId == 3) return sphericalSurface2(u, v);
    if (uSurfaceId == 4) return cylindricalSurface1(u, v);
    if (uSurfaceId == 5) return cylindricalSurface2(u, v);

    // fallback – rovinný grid v [-1,1]^2
    float x = mix(-1.0, 1.0, u);
    float y = mix(-1.0, 1.0, v);
    return vec3(x, 0.0, y);
}
vec3 computeNormal(float u, float v) {
    float e = 0.002;
    float u1 = clamp(u + e, 0.0, 1.0);
    float v1 = clamp(v + e, 0.0, 1.0);

    vec3 p  = surfacePosition(u,  v);
    vec3 pu = surfacePosition(u1, v)  - p;
    vec3 pv = surfacePosition(u,  v1) - p;

    return normalize(cross(pu, pv));
}


void main()
{
    float u = inPosition.x;
    float v = inPosition.y;

    texCoord = inPosition;

    vec3 posLocal = surfacePosition(u, v);
    vec3 nLocal   = computeNormal(u, v);

    vec4 wp = uModel * vec4(posLocal, 1.0);
    worldPos = wp.xyz;
    vec3 worldNormal = mat3(uModel) * nLocal;

    gl_Position = uProj * uView * wp;

    lightVector  = uLightPos - worldPos;
    normalVector = worldNormal;
}
