package app.solid;

import lwjglutils.OGLBuffers;

public class GridStrip extends Solid{
    private final OGLBuffers buffers;

    public GridStrip(int m, int n) {
        float[] vb = new float[2 * m * n];
        int index = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                vb[index++] = j / (float) (n - 1);
                vb[index++] = i / (float) (m - 1);
            }
        }

        int numStrips = m - 1;
        int vertsPerStrip = 2 * n;
        int degens = 2 * (numStrips - 1);
        int[] ib = new int[numStrips * vertsPerStrip + degens];

        int k = 0;
        for (int i = 0; i < m - 1; i++) {
            if (i > 0) {
                ib[k++] = i * n;
            }

            for (int j = 0; j < n; j++) {
                int row1 = i * n + j;
                int row2 = (i + 1) * n + j;
                ib[k++] = row1;
                ib[k++] = row2;
            }

            if (i < m - 2) {
                ib[k++] = (i + 1) * n + (n - 1);
            }
        }

        OGLBuffers.Attrib[] attributes = new OGLBuffers.Attrib[] {
                new OGLBuffers.Attrib("inPosition", 2)
        };

        buffers = new OGLBuffers(vb, attributes, ib);
    }

    public OGLBuffers getBuffers() {
        return buffers;
    }
}
