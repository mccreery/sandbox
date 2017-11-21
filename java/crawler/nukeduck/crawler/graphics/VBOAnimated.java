package nukeduck.crawler.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;


public class VBOAnimated extends VBO {
	protected int vertsPerFrame;

	public VBOAnimated(IAnimated generator) {
		super(generator);
	}

	public void prepare() {
		super.prepare();
		this.vertsPerFrame = ((IAnimated) this.generator).facesPerFrame() * 3;
	}

	@Override
	public void render() {
		this.render(0);
	}
	public void render(int frame) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboId);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

		GL11.glVertexPointer(2, GL11.GL_INT, 20, 0);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 8);
		GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 20, 16);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, this.vertsPerFrame * frame, this.vertsPerFrame);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public static abstract class IAnimated extends IFaces {
		public IAnimated(int perFace) {
			super(perFace);
		}

		public abstract VertexData[] getFace(int i);
		public abstract int getFaceCount();

		public abstract int facesPerFrame();
	}
}
