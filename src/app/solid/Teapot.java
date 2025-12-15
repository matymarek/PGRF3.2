package app.solid;

import app.solid.util.Mesh;
import app.solid.util.TeapotData;
import lwjglutils.OGLBuffers;

public class Teapot extends Solid {

    private int resolution;

    public Teapot(int resolution) {
        this.resolution = Math.max(1, resolution);
        rebuild();
    }

    public void setResolution(int resolution) {
        int r = Math.max(1, resolution);
        if (r == this.resolution) return;
        this.resolution = r;
        rebuild();
    }

    private void rebuild() {
        Mesh m = TeapotData.build(resolution);

        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3),
                new OGLBuffers.Attrib("inNormal", 3),
                new OGLBuffers.Attrib("inTexCoord", 2),
        };

        buffers = new OGLBuffers(m.vb, attributes, m.ib);
    }
}
