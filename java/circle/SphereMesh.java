import org.lwjgl.opengl.GL11;

/** Mesh representing a sphere with the given number of
 * latitude and longitude divisions */
public class SphereMesh extends Mesh {
	/** Lats: The number of latitudinal quads
	 * Longs: The number of longitudinal quads */
	private final int lats, longs;

	public SphereMesh(int lats, int longs) {
		super(true);
		setMode(GL11.GL_QUADS);

		this.lats = lats;
		this.longs = longs;
	}

	@Override
	public Vertex[] getVertices() {
		// 4 verts per quad
		Vertex[] vertices = new Vertex[lats * longs * 4];
		int i = 0;

		float pitch = -(float)Math.PI, yaw;
		float pitchDelta = (2*(float)Math.PI) / lats;
		float yawDelta = (2*(float)Math.PI) / longs;

		float x, y, z, scale, yNext, scaleNext, mag;
		yNext = (float)Math.sin(pitch);
		scaleNext = (float)Math.cos(pitch);
		pitch += pitchDelta;

		for(int lat = 0; lat < lats; lat++, pitch += pitchDelta) {
			y = yNext;
			scale = scaleNext;

			yNext = (float)Math.sin(pitch);
			scaleNext = (float)Math.cos(pitch);

			yaw = 0;
			for(int lon = 0; lon < longs; lon++) { // Can't use "long" keyword
				// 1 quad is added per loop
				x = (float)Math.cos(yaw);
				z = (float)Math.sin(yaw);

				// Bottom left
				mag = (float)Math.sqrt((x*x + z*z)*scale*scale + y*y);
				vertices[i] = new Vertex(x*scale, y, z*scale, x*scale / mag, y / mag, z*scale / mag);

				// Top left
				mag = (float)Math.sqrt((x*x + z*z)*scaleNext*scaleNext + yNext*yNext);
				vertices[i+2] = new Vertex(x*scaleNext, yNext, z*scaleNext, x*scaleNext / mag, yNext / mag, z*scaleNext / mag);

				yaw += yawDelta;
				x = (float)Math.cos(yaw);
				z = (float)Math.sin(yaw);

				// Bottom right
				mag = (float)Math.sqrt((x*x + z*z)*scale*scale + y*y);
				vertices[i+1] = new Vertex(x*scale, y, z*scale, x*scale / mag, y / mag, z*scale / mag);

				// Top right
				mag = (float)Math.sqrt((x*x + z*z)*scaleNext*scaleNext + yNext*yNext);
				vertices[i+3] = new Vertex(x*scaleNext, yNext, z*scaleNext, x*scaleNext / mag, yNext / mag, z*scaleNext / mag);

				i += 4;
			}
		}
		return vertices;
	}
}
