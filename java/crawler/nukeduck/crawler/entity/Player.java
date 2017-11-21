package nukeduck.crawler.entity;

import java.util.List;

import nukeduck.crawler.Crawler;
import nukeduck.crawler.graphics.Color;
import nukeduck.crawler.graphics.RenderUtil;
import nukeduck.crawler.graphics.Texture;
import nukeduck.crawler.graphics.VBO.VertexData;
import nukeduck.crawler.graphics.VBOAnimated;
import nukeduck.crawler.graphics.VBOAnimated.IAnimated;
import nukeduck.crawler.util.BoundingBox;
import nukeduck.crawler.util.Keys;
import nukeduck.crawler.util.ResourcePath;
import nukeduck.crawler.util.Vec2;

import org.lwjgl.opengl.GL11;

public class Player extends Entity {
	public static Texture sprites;
	VBOAnimated frames;

	float yVel = 0;

	public Player() {
		super(100);
		if(sprites == null) {
			sprites = new Texture(new ResourcePath(ResourcePath.TEXTURES, "player.png"));
		}
		frames = new VBOAnimated(new IAnimated(3) {
			@Override
			public VertexData[] getFace(int i) {
				boolean flag = i % 2 == 1;
				int face = i / 2;

				float u = face * RenderUtil.TILE_SIZE;
				float v = 0;

				if(flag) {
					return new VertexData[] {
						new VertexData(new Vec2(0, 0), u, v, Color.WHITE),
						new VertexData(new Vec2(16, 16), u + RenderUtil.TILE_SIZE, v + RenderUtil.TILE_SIZE, Color.WHITE),
						new VertexData(new Vec2(0, 16), u, v + RenderUtil.TILE_SIZE, Color.WHITE)
					};
				} else {
					return new VertexData[] {
						new VertexData(new Vec2(0, 0), u, v, Color.WHITE),
						new VertexData(new Vec2(16, 0), u + RenderUtil.TILE_SIZE, v, Color.WHITE),
						new VertexData(new Vec2(16, 16), u + RenderUtil.TILE_SIZE, v + RenderUtil.TILE_SIZE, Color.WHITE)
					};
				}
			}

			@Override
			public int getFaceCount() {
				return 14;
			}

			@Override
			public int facesPerFrame() {
				return 2;
			}
		});
		frames.prepare();
	}

	public void handleInput() {
		this.yVel -= 0.1f;
		BoundingBox last = this.pos.clone();

		if(Keys.A.state) {
			this.pos.offset(new Vec2(-1, 0));
		}
		if(Keys.D.state) {
			this.pos.offset(new Vec2(1, 0));
		}
		if(Keys.W.state) {
			this.yVel = 2.0f;
		}
		if(Keys.S.state) {
			this.pos.offset(new Vec2(0, 1));
		}
		this.pos.offset(new Vec2(0, -this.yVel));
		List<BoundingBox> boxes = Crawler.INSTANCE.map.getBoundingBoxes();
		boolean a = false;
		for(BoundingBox box : boxes) {
			box.collideX(last, this.pos);
			if(box.collideY(last, this.pos)) a = true;
		}
		if(a) this.yVel = 0;
		/*for(BoundingBox box : boxes) {
			box.collideY(last, this.pos);
		}*/
	}

	@Override
	public void render(float partialTicks) {
		GL11.glTranslatef(this.pos.min.x, this.pos.min.y, 0);
		sprites.bind();
		this.frames.render(Crawler.INSTANCE.time % 7);
	}
}
