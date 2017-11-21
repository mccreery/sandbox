package nukeduck.crawler.graphics;

import java.nio.ByteBuffer;

import nukeduck.crawler.util.Vec2;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class VBO {
	protected IVertices generator;
	public int vertexCount;
	protected int vboId;

	public VBO(IVertices generator) {
		this.generator = generator;
	}

	public void prepare() {
		this.vertexCount = this.generator.getVertexCount();
		ByteBuffer data = BufferUtils.createByteBuffer(this.vertexCount * VertexData.LENGTH);

		for(int i = 0; i < this.vertexCount; i++) {
			VertexData vertex = this.generator.getVertex(i);

			data.putInt((int) vertex.pos.x)
				.putInt((int) vertex.pos.y);
			data.putFloat(vertex.u)
				.putFloat(vertex.v);
			data.putInt(vertex.color.pack());
		}

		data.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboId = GL15.glGenBuffers());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public void render() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboId);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		//GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

		GL11.glVertexPointer(2, GL11.GL_INT, 20, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 8);
		//GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 20, 16);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertexCount);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		//GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public static class VertexData {
		public static final int LENGTH =
			8 +	// Position - two ints
			8 +	// UV - two floats
			4;	// Color - 1 packed int

		public VertexData(Vec2 pos, float u, float v, Color color) {
			this.pos = pos;
			this.u = u;
			this.v = v;
			this.color = color;
		}

		public Vec2 pos;
		public float u, v;
		public Color color;
	}

	public static interface IVertices {
		public int getVertexCount();
		public VertexData getVertex(int i);
	}

	public static abstract class IFaces implements IVertices {
		private int perFace;

		private int faceIndex = 0;
		private VertexData[] currentFace;

		public IFaces(int perFace) {
			this.perFace = perFace;
		}

		public abstract VertexData[] getFace(int i);
		public abstract int getFaceCount();

		public int getVertexCount() {
			return this.perFace * this.getFaceCount();
		}

		public VertexData getVertex(int i) {
			int vert = i % this.perFace;
			if(vert == 0) this.currentFace = this.getFace(this.faceIndex++);

			return this.currentFace[vert];
		}
	}
}
