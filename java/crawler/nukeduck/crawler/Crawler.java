package nukeduck.crawler;

import nukeduck.crawler.Launch.LaunchArgs;
import nukeduck.crawler.entity.Player;
import nukeduck.crawler.event.Events;
import nukeduck.crawler.graphics.DebugHandler;
import nukeduck.crawler.graphics.HashedText;
import nukeduck.crawler.graphics.Texture;
import nukeduck.crawler.graphics.TextureFont;
import nukeduck.crawler.graphics.VBO;
import nukeduck.crawler.graphics.WindowHandle;
import nukeduck.crawler.gui.GuiInputs;
import nukeduck.crawler.gui.GuiOverlay;
import nukeduck.crawler.gui.IGuiLayer;
import nukeduck.crawler.map.Map;
import nukeduck.crawler.util.FramerateMonitor;
import nukeduck.crawler.util.GLUtil;
import nukeduck.crawler.util.Keys;
import nukeduck.crawler.util.RepeatingEvent;
import nukeduck.crawler.util.ResourcePath;
import nukeduck.crawler.util.Vec2;

import org.lwjgl.opengl.GL11;

public class Crawler {
	private WindowHandle handle;
	public TextureFont font;
	public Texture tileTexture;
	public DebugHandler debug;

	public IGuiLayer baseGui = new GuiOverlay();
	public Map map;
	public VBO nannyText;
	public HashedText textCache = new HashedText(256);

	public Player player;

	public static Crawler INSTANCE;

	public Crawler() {
		if(INSTANCE == null) INSTANCE = this;
	}

	public void start(LaunchArgs args) {
		Events.init();

		this.handle = new WindowHandle(args);
		handle.center();
		GLUtil.setup2D(handle);

		this.baseGui.push(new GuiInputs());

		this.player = new Player();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		//GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glColor3f(1.0F, 1.0F, 1.0F);

		font = new TextureFont(new ResourcePath(ResourcePath.TEXTURES, "font_space.png"));
		this.tileTexture = new Texture(new ResourcePath(ResourcePath.TEXTURES, "tiles.png"));

		//this.nannyText = new VBO(new TextMesh(this.font, navy));
		//this.nannyText.prepare();

		/*this.map = new Map(100, 100);
		for(int x = 0; x < map.getSize().x; x++) {
			for(int y = 0; y < map.getSize().y; y++) {
				if(Math.random() < 0.5) map.setTile(x, y, Tile.redBrick);
			}
		}*/
		this.map = new Map(new ResourcePath(ResourcePath.TEXTURES, "level.png"));
		this.map.prepare();

		long time = System.nanoTime();
		long lastTickTime = time;

		while (!handle.shouldClose()) {
			this.monitor.add(-time + (time = System.nanoTime()));

			long partial;
			while((partial = time - lastTickTime) >= TICK_DELAY) {
				tick();
				lastTickTime += TICK_DELAY;
			}
			render((float) partial / (float) TICK_DELAY);
		}

		handle.destroy();
	}

	//String test = "testing string";

	private FramerateMonitor monitor = new FramerateMonitor(100);
	//long[] rollingFrames = new long[1000];

	public static final long TICK_DELAY = 10000000; // 100 ticks/second
	public int time = 0;

	public void tick() {
		ha.tick();
		this.monitor.tick();
		player.handleInput();
	}

	RepeatingEvent ha = new RepeatingEvent(10) {
		@Override
		public void call() {
			time++;
		}
	};

	public void render(float partialTicks) {
		handle.clear();
		GL11.glClearColor(0.1F, 0.1F, 0.1F, 0.0F);

		//Profiler p = new Profiler(3).reset();
		GL11.glPushMatrix();

		//GL11.glTranslatef(0, (handle.getSize().y - RenderUtil.TEXT_HEIGHT) / 2, 0);
		GL11.glScalef(2, 2, 2);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.tileTexture.bind();
		this.map.render(partialTicks);
		this.baseGui.renderTree(this.handle.getSize());

		this.player.render(partialTicks);
		//p.lap();
		//GL11.glTranslatef((float) (-frames / 3) * 5.6F, 0, 0);
		//GL11.glColor3f(1.0F, (float) handle.mousePos.x / handle.getSize().x, (float) handle.mousePos.y / handle.getSize().y);
		//if(frames % 3 == 0) s.append(navy.charAt(s.length() % navy.length()));

		/*List<CharSequence> split = FuncsUtil.split(this.font, navy, handle.getSize().x);
		for(CharSequence s : split) {
			this.textCache.get(s).render();
			GL11.glTranslatef(0, 8, 0);
		}*/

		/*for(int i = 0; i < 20; i++) {
			this.textCache.get(String.valueOf(i)).render();
			GL11.glTranslatef(0, 8, 0);
		}*/
		//RenderUtil.drawString(font, new Vec2(4, 4), String.valueOf(time), new TextFormatterColor(Color.WHITE));

		//RenderUtil.drawString(font, new Vec2(4, 4), String.valueOf(this.monitor.getFramerate()), new TextFormatterColor(Color.WHITE));

		GL11.glPopMatrix();

		//layer.renderTree(this.handle.getSize());
		//p.lap();

		handle.update();
		handle.pollEvents();
		//p.lap();
		
		//double[] times = p.normalizedTotal();
		//System.out.printf("Map: %.2f Text: %.2f Swap: %.2f\n", times[0], times[1], times[2]);
		//frames++;
	}
}
