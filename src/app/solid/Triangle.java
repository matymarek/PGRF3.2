package app.solid;

import lwjglutils.OGLBuffers;

public class Triangle extends Solid {

    public Triangle() {
        float[] vb = {
                0.5f,  1, 0,    1, 0, 0,
                -0.5f,  1, 0,    0, 1, 0,
                0, 0, 0,    0, 0, 1,
        };

        int[] ib = {
                0, 1, 2
        };

        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3),
                new OGLBuffers.Attrib("inColor", 3),
        };

        buffers = new OGLBuffers(vb, attributes, ib);

    }
}
