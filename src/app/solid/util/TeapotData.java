package app.solid.util;

import java.util.ArrayList;
import java.util.List;

public class TeapotData {
    // Rim(1), Body(2), Lid(2), Handle(2), Spout(2), Bottom(1)
    public static final int[][] PATCHES = {
            // Rim
            {102,103,104,105,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15},

            // Body
            { 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27},
            { 24, 25, 26, 27, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40},

            // Lid
            { 96, 96, 96, 96, 97, 98, 99,100,101,101,101,101,  0,  1,  2,  3},
            {  0,  1,  2,  3,106,107,108,109,110,111,112,113,114,115,116,117},

            // Handle
            { 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56},
            { 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 28, 65, 66, 67},

            // Spout
            { 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83},
            { 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95},

            // Bottom
            {118,118,118,118,124,122,119,121,123,126,125,120, 40, 39, 38, 37},
    };

    public static final float[][] VERTICES = {
            { 0.2000f,  0.0000f, 2.70000f}, { 0.2000f, -0.1120f, 2.70000f},
            { 0.1120f, -0.2000f, 2.70000f}, { 0.0000f, -0.2000f, 2.70000f},
            { 1.3375f,  0.0000f, 2.53125f}, { 1.3375f, -0.7490f, 2.53125f},
            { 0.7490f, -1.3375f, 2.53125f}, { 0.0000f, -1.3375f, 2.53125f},
            { 1.4375f,  0.0000f, 2.53125f}, { 1.4375f, -0.8050f, 2.53125f},
            { 0.8050f, -1.4375f, 2.53125f}, { 0.0000f, -1.4375f, 2.53125f},
            { 1.5000f,  0.0000f, 2.40000f}, { 1.5000f, -0.8400f, 2.40000f},
            { 0.8400f, -1.5000f, 2.40000f}, { 0.0000f, -1.5000f, 2.40000f},
            { 1.7500f,  0.0000f, 1.87500f}, { 1.7500f, -0.9800f, 1.87500f},
            { 0.9800f, -1.7500f, 1.87500f}, { 0.0000f, -1.7500f, 1.87500f},
            { 2.0000f,  0.0000f, 1.35000f}, { 2.0000f, -1.1200f, 1.35000f},
            { 1.1200f, -2.0000f, 1.35000f}, { 0.0000f, -2.0000f, 1.35000f},
            { 2.0000f,  0.0000f, 0.90000f}, { 2.0000f, -1.1200f, 0.90000f},
            { 1.1200f, -2.0000f, 0.90000f}, { 0.0000f, -2.0000f, 0.90000f},
            {-2.0000f,  0.0000f, 0.90000f}, { 2.0000f,  0.0000f, 0.45000f},
            { 2.0000f, -1.1200f, 0.45000f}, { 1.1200f, -2.0000f, 0.45000f},
            { 0.0000f, -2.0000f, 0.45000f}, { 1.5000f,  0.0000f, 0.22500f},
            { 1.5000f, -0.8400f, 0.22500f}, { 0.8400f, -1.5000f, 0.22500f},
            { 0.0000f, -1.5000f, 0.22500f}, { 1.5000f,  0.0000f, 0.15000f},
            { 1.5000f, -0.8400f, 0.15000f}, { 0.8400f, -1.5000f, 0.15000f},
            { 0.0000f, -1.5000f, 0.15000f}, {-1.6000f,  0.0000f, 2.02500f},
            {-1.6000f, -0.3000f, 2.02500f}, {-1.5000f, -0.3000f, 2.25000f},
            {-1.5000f,  0.0000f, 2.25000f}, {-2.3000f,  0.0000f, 2.02500f},
            {-2.3000f, -0.3000f, 2.02500f}, {-2.5000f, -0.3000f, 2.25000f},
            {-2.5000f,  0.0000f, 2.25000f}, {-2.7000f,  0.0000f, 2.02500f},
            {-2.7000f, -0.3000f, 2.02500f}, {-3.0000f, -0.3000f, 2.25000f},
            {-3.0000f,  0.0000f, 2.25000f}, {-2.7000f,  0.0000f, 1.80000f},
            {-2.7000f, -0.3000f, 1.80000f}, {-3.0000f, -0.3000f, 1.80000f},
            {-3.0000f,  0.0000f, 1.80000f}, {-2.7000f,  0.0000f, 1.57500f},
            {-2.7000f, -0.3000f, 1.57500f}, {-3.0000f, -0.3000f, 1.35000f},
            {-3.0000f,  0.0000f, 1.35000f}, {-2.5000f,  0.0000f, 1.12500f},
            {-2.5000f, -0.3000f, 1.12500f}, {-2.6500f, -0.3000f, 0.93750f},
            {-2.6500f,  0.0000f, 0.93750f}, {-2.0000f, -0.3000f, 0.90000f},
            {-1.9000f, -0.3000f, 0.60000f}, {-1.9000f,  0.0000f, 0.60000f},
            { 1.7000f,  0.0000f, 1.42500f}, { 1.7000f, -0.6600f, 1.42500f},
            { 1.7000f, -0.6600f, 0.60000f}, { 1.7000f,  0.0000f, 0.60000f},
            { 2.6000f,  0.0000f, 1.42500f}, { 2.6000f, -0.6600f, 1.42500f},
            { 3.1000f, -0.6600f, 0.82500f}, { 3.1000f,  0.0000f, 0.82500f},
            { 2.3000f,  0.0000f, 2.10000f}, { 2.3000f, -0.2500f, 2.10000f},
            { 2.4000f, -0.2500f, 2.02500f}, { 2.4000f,  0.0000f, 2.02500f},
            { 2.7000f,  0.0000f, 2.40000f}, { 2.7000f, -0.2500f, 2.40000f},
            { 3.3000f, -0.2500f, 2.40000f}, { 3.3000f,  0.0000f, 2.40000f},
            { 2.8000f,  0.0000f, 2.47500f}, { 2.8000f, -0.2500f, 2.47500f},
            { 3.5250f, -0.2500f, 2.49375f}, { 3.5250f,  0.0000f, 2.49375f},
            { 2.9000f,  0.0000f, 2.47500f}, { 2.9000f, -0.1500f, 2.47500f},
            { 3.4500f, -0.1500f, 2.51250f}, { 3.4500f,  0.0000f, 2.51250f},
            { 2.8000f,  0.0000f, 2.40000f}, { 2.8000f, -0.1500f, 2.40000f},
            { 3.2000f, -0.1500f, 2.40000f}, { 3.2000f,  0.0000f, 2.40000f},
            { 0.0000f,  0.0000f, 3.15000f}, { 0.8000f,  0.0000f, 3.15000f},
            { 0.8000f, -0.4500f, 3.15000f}, { 0.4500f, -0.8000f, 3.15000f},
            { 0.0000f, -0.8000f, 3.15000f}, { 0.0000f,  0.0000f, 2.85000f},
            { 1.4000f,  0.0000f, 2.40000f}, { 1.4000f, -0.7840f, 2.40000f},
            { 0.7840f, -1.4000f, 2.40000f}, { 0.0000f, -1.4000f, 2.40000f},
            { 0.4000f,  0.0000f, 2.55000f}, { 0.4000f, -0.2240f, 2.55000f},
            { 0.2240f, -0.4000f, 2.55000f}, { 0.0000f, -0.4000f, 2.55000f},
            { 1.3000f,  0.0000f, 2.55000f}, { 1.3000f, -0.7280f, 2.55000f},
            { 0.7280f, -1.3000f, 2.55000f}, { 0.0000f, -1.3000f, 2.55000f},
            { 1.3000f,  0.0000f, 2.40000f}, { 1.3000f, -0.7280f, 2.40000f},
            { 0.7280f, -1.3000f, 2.40000f}, { 0.0000f, -1.3000f, 2.40000f},
            { 0.0000f,  0.0000f, 0.00000f}, { 1.4250f, -0.7980f, 0.00000f},
            { 1.5000f,  0.0000f, 0.07500f}, { 1.4250f,  0.0000f, 0.00000f},
            { 0.7980f, -1.4250f, 0.00000f}, { 0.0000f, -1.5000f, 0.07500f},
            { 0.0000f, -1.4250f, 0.00000f}, { 1.5000f, -0.8400f, 0.07500f},
            { 0.8400f, -1.5000f, 0.07500f},
    };


    public static Mesh build(int divs) {
        divs = Math.max(1, divs);

        List<Float> vb = new ArrayList<>();
        List<Integer> ib = new ArrayList<>();

        int vertexBase = 0;

        for (int p = 0; p < PATCHES.length; p++) {
            boolean mirrorXY = (p <= 4) || (p == 9);

            int[][] variants;
            if (mirrorXY) {
                variants = new int[][] { {+1,+1}, {-1,+1}, {+1,-1}, {-1,-1} };
            } else {
                variants = new int[][] { {+1,+1}, {+1,-1} }; // x stays, y flips
            }

            for (int[] v : variants) {
                int sx = v[0];
                int sy = v[1];

                // grid vertices for this patch
                int patchVertexStart = vertexBase;
                for (int j = 0; j <= divs; j++) {
                    float vv = j / (float) divs;
                    for (int i = 0; i <= divs; i++) {
                        float uu = i / (float) divs;

                        Eval e = evalPatch(PATCHES[p], uu, vv, sx, sy);

                        // pos
                        vb.add(e.px); vb.add(e.py); vb.add(e.pz);
                        // normal
                        vb.add(e.nx); vb.add(e.ny); vb.add(e.nz);
                        // uv
                        vb.add(uu); vb.add(vv);

                        vertexBase++;
                    }
                }

                // indices (2 triangles per quad)
                int row = divs + 1;
                for (int j = 0; j < divs; j++) {
                    for (int i = 0; i < divs; i++) {
                        int a = patchVertexStart + j * row + i;
                        int b = patchVertexStart + (j + 1) * row + i;
                        int c = patchVertexStart + (j + 1) * row + (i + 1);
                        int d = patchVertexStart + j * row + (i + 1);

                        ib.add(a); ib.add(b); ib.add(c);
                        ib.add(a); ib.add(c); ib.add(d);
                    }
                }
            }
        }

        float[] vbArr = new float[vb.size()];
        for (int i = 0; i < vb.size(); i++) vbArr[i] = vb.get(i);

        int[] ibArr = new int[ib.size()];
        for (int i = 0; i < ib.size(); i++) ibArr[i] = ib.get(i);

        return new Mesh(vbArr, ibArr);
    }

    private static Eval evalPatch(int[] patch, float u, float v, int sx, int sy) {
        float[] Bu = bernstein(u);
        float[] Bv = bernstein(v);
        float[] dBu = bernsteinDeriv(u);
        float[] dBv = bernsteinDeriv(v);

        float px=0,py=0,pz=0;
        float dux=0, duy=0, duz=0;
        float dvx=0, dvy=0, dvz=0;

        for (int iu = 0; iu < 4; iu++) {
            for (int iv = 0; iv < 4; iv++) {
                int idx = patch[iu*4 + iv];
                float x = VERTICES[idx][0] * sx;
                float y = VERTICES[idx][1] * sy;
                float z = VERTICES[idx][2];

                float b = Bu[iu] * Bv[iv];
                px += b*x; py += b*y; pz += b*z;

                float bu = dBu[iu] * Bv[iv];
                dux += bu*x; duy += bu*y; duz += bu*z;

                float bv = Bu[iu] * dBv[iv];
                dvx += bv*x; dvy += bv*y; dvz += bv*z;
            }
        }

        // normals
        float nx = duy*dvz - duz*dvy;
        float ny = duz*dvx - dux*dvz;
        float nz = dux*dvy - duy*dvx;

        nx = -nx;
        ny = -ny;
        nz = -nz;

        if (sx * sy < 0) {
            nx = -nx;
            ny = -ny;
            nz = -nz;
        }

        float len = (float)Math.sqrt(nx*nx + ny*ny + nz*nz);
        if (len < 1e-8f) { nx=0; ny=0; nz=1; len=1; }
        nx /= len; ny /= len; nz /= len;

        Eval e = new Eval();
        e.px=px; e.py=py; e.pz=pz;
        e.nx=nx; e.ny=ny; e.nz=nz;
        return e;
    }

    private static float[] bernstein(float t) {
        float it = 1f - t;
        float b0 = it*it*it;
        float b1 = 3f*t*it*it;
        float b2 = 3f*t*t*it;
        float b3 = t*t*t;
        return new float[]{b0,b1,b2,b3};
    }

    private static float[] bernsteinDeriv(float t) {
        // derivace kubických Bernsteinových polynomů
        float it = 1f - t;
        float d0 = -3f*it*it;
        float d1 = 3f*it*it - 6f*t*it;
        float d2 = 6f*t*it - 3f*t*t;
        float d3 = 3f*t*t;
        return new float[]{d0,d1,d2,d3};
    }
}
