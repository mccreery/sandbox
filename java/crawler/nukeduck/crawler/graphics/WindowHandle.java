package nukeduck.crawler.graphics;

import java.nio.ByteBuffer;
import java.util.HashMap;

import nukeduck.crawler.Launch.LaunchArgs;
import nukeduck.crawler.event.input.KeyEvent;
import nukeduck.crawler.event.input.KeyEvents;
import nukeduck.crawler.event.input.MouseEvent;
import nukeduck.crawler.event.input.MouseEvents;
import nukeduck.crawler.event.input.KeyEvent.KeyAction;
import nukeduck.crawler.event.input.MouseEvent.ClickEvent;
import nukeduck.crawler.event.input.MouseEvent.MouseButton;
import nukeduck.crawler.event.input.MouseEvent.MouseDownEvent;
import nukeduck.crawler.event.input.MouseEvent.MouseUpEvent;
import nukeduck.crawler.util.GLUtil;
import nukeduck.crawler.util.Keys;
import nukeduck.crawler.util.Variables;
import nukeduck.crawler.util.Vec2;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowHandle {
	public LaunchArgs args;

	private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			Keys keyObj = Keys.fromId(key);
			KeyAction actionObj = KeyAction.fromId(action);

			if(actionObj == KeyAction.PRESS) keyObj.state = true;
			else if(actionObj == KeyAction.RELEASE) keyObj.state = false;

			KeyEvents.INSTANCE.post(new KeyEvent(keyObj, actionObj));
		}
	};
	private GLFWErrorCallback errorCallback = new GLFWErrorCallback() {
		@Override
		public void invoke(int error, long description) {
			System.err.println("It's fucked mate: " + error);
		}
	};
	private GLFWWindowSizeCallback resizeCallback = new GLFWWindowSizeCallback() {
		private WindowHandle window;

		@Override
		public void invoke(long window, int width, int height) {
			this.window.args.size = new Vec2(width, height);
			GLUtil.setup2D(this.window);
		}

		public GLFWWindowSizeCallback setWindow(WindowHandle window) {
			this.window = window;
			return this;
		}
	}.setWindow(this);

	public Vec2 mousePos = new Vec2(0, 0);
	public HashMap<MouseButton, Boolean> mouseButtons = new HashMap<MouseButton, Boolean>();
	private Vec2 clickStart;

	private GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
		@Override
		public void invoke(long window, double xpos, double ypos) {
			MouseEvents.INSTANCE.post(new MouseEvent.MouseMoveEvent(mousePos.clone(), mousePos = new Vec2((int) xpos, (int) ypos)));
		}
	};
	private GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			MouseButton b = MouseButton.get(button);

			if(action == 1) {
				clickStart = mousePos.clone();
				mouseButtons.put(b, true);
				MouseEvents.INSTANCE.post(new MouseDownEvent(mousePos, b));
			} else {
				mouseButtons.put(b, false);
				MouseEvents.INSTANCE.post(new MouseUpEvent(mousePos, b));
				MouseEvents.INSTANCE.post(new ClickEvent(clickStart, mousePos, b));
			}
		}
	};

	private long handle;

	public WindowHandle(LaunchArgs args) {
		for(MouseButton button : MouseButton.values()) {
			mouseButtons.put(button, false);
		}
		this.args = args;

		GLFW.glfwSetErrorCallback(this.errorCallback);
		if (GLFW.glfwInit() != GL11.GL_TRUE )
			throw new IllegalStateException("Unable to initialize GLFW");

		long monitor = MemoryUtil.NULL;
		if(args.fullscreen) {
			PointerBuffer monitors = GLFW.glfwGetMonitors();
			if(args.monitorId < monitors.capacity()) {
				monitor = monitors.get(args.monitorId);
			}
		}
		this.handle = GLFW.glfwCreateWindow((int) args.size.x, (int) args.size.y, Variables.getWindowTitle(), monitor, MemoryUtil.NULL);

		GLFW.glfwSetKeyCallback(this.getHandle(), this.keyCallback);
		GLFW.glfwSetWindowSizeCallback(this.getHandle(), this.resizeCallback);
		GLFW.glfwSetCursorPosCallback(this.getHandle(), this.cursorPosCallback);
		GLFW.glfwSetMouseButtonCallback(this.getHandle(), this.mouseButtonCallback);

		GLFW.glfwMakeContextCurrent(this.getHandle());
		//GLFW.glfwSwapInterval(1);

		GL.createCapabilities();
	}

	public long getHandle() {
		return this.handle;
	}

	public Vec2 getSize() {
		return this.args.size;
	}

	public void setVisible(boolean visible) {
		if(visible) {
			GLFW.glfwShowWindow(this.getHandle());
		} else {
			GLFW.glfwHideWindow(this.getHandle());
		}
	}

	public void update() {
		GLFW.glfwSwapBuffers(this.getHandle());
	}

	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void pollEvents() {
		GLFW.glfwPollEvents();
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(this.getHandle()) == GL11.GL_TRUE;
	}

	public void destroy() {
		GLFW.glfwDestroyWindow(this.getHandle());
		this.keyCallback.release();
		GLFW.glfwTerminate();
		this.errorCallback.release();
	}

	public void center() {
		Vec2 size = this.getSize();
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		Vec2 vec2 = new Vec2((GLFWvidmode.width(vidmode) - size.x) / 2, (GLFWvidmode.height(vidmode) - size.y) / 2);
		this.setPos(vec2);
	}

	public void setPos(Vec2 pos) {
		GLFW.glfwSetWindowPos(this.getHandle(), (int) pos.x, (int) pos.y);
	}
}
