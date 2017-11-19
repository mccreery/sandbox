import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public abstract class Mesh {
	/** OpenGL ID for the VAO */
	private final int id;

	/** Main OpenGL buffer to hold vertices and indices */
	private final int buffer;

	/** The drawing mode for this mesh */
	private int mode = GL11.GL_TRIANGLES;
	private int vertexCount = 0;

	public Mesh(boolean build) {
		id = GL30.glGenVertexArrays();
		buffer = GL15.glGenBuffers();

		if(build) build();
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	/** @return An array of {@link #getVertexCount()} vertices */
	public abstract Vertex[] getVertices();

	/** The number of bytes a single vertex takes up (9 floats) */
	private static final int STRIDE = 6*4;
	/** The number of bytes a single attribute of a vertex takes up (3 floats) */
	private static final int OFFSET = 3*4;

	/** Builds and uploads the mesh to the GPU */
	public void build() {
		Vertex[] vertices = getVertices();
		vertexCount = vertices.length;

		// 4 bytes to a float, 6 floats per vertex
		ByteBuffer buf = ByteBuffer.allocateDirect(vertexCount * STRIDE);
		for(Vertex v : vertices) {
			v.serialize(buf); // Fill the buf with vertices
		}
		buf.flip();

		// Upload it to OpenGL
		bind();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);

		// Remember where our attribs are
		for(int i = 0; i < 2; i++) {
			GL20.glEnableVertexAttribArray(i);
			GL20.glVertexAttribPointer(i, 3, GL11.GL_FLOAT, false, STRIDE, i*OFFSET);
		}
	}

	public void draw() {
		bind();
		GL11.glDrawArrays(mode, 0, vertexCount);
	}

	/** The current bound VAO ID */
	private static int bound = 0;

	/** Binds this mesh's VAO for drawing */
	public void bind() {
		if(bound != id) {
			GL30.glBindVertexArray(bound = id);
		}
	}

	/** Unbinds the current mesh VAO */
	public static void unbind() {
		GL30.glBindVertexArray(bound = 0);
	}

	/** Contains information about a single vertex in a mesh */
	public static class Vertex {
		public final float x, y, z;
		public final float nx, ny, nz;

		public Vertex(float x, float y, float z,
				float nx, float ny, float nz) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.nx = nx;
			this.ny = ny;
			this.nz = nz;
		}

		public void serialize(ByteBuffer buf) {
			buf.putFloat(x).putFloat(y).putFloat(z);
			buf.putFloat(nx).putFloat(ny).putFloat(nz);
		}
	}
}
