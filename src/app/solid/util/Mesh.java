package app.solid.util;

public class Mesh {
    public final float[] vb; // interleaved: pos(3) normal(3) uv(2)
    public final int[] ib;
    public Mesh(float[] vb, int[] ib) { this.vb = vb; this.ib = ib; }
}
