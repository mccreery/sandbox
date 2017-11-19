import java.io.IOException;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

/** The window is divided inot 4 equally-sized panes.<br>
 * Each pane contains an image, and the window will letterbox to make sure
 * the panes maintain their aspect ratio when tiled in a 2x2.<br>
 * The windows are:
 * <ol>
 * <li>The raw webcam footage from the user
 * <li>The edge-detected footage
 * <li>The hough transform visualisation
 * <li>3D rendered (guessed) spheres
 * </ol>*/
public class CircleFinder {
	/** The title of the window */
	private static final String TITLE = "Circle Detector";

	/** Will be rendered individually and composited on-screen */
	private int[] textures = new int[4];
	private int fbo;

	private final ShaderProgram program;
	private final Mesh sphere;

	/** A handle to the window, given by {@link #glfwCreateWindow} */
	private final long handle;

	public CircleFinder() throws IOException {
		handle = GLFW.glfwCreateWindow(1280, 720, TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
		GLFW.glfwShowWindow(handle);

		program = new ShaderProgram("main.vert", "main.frag");
		sphere = new SphereMesh(5, 5);

		initFBO();
		loop();
		deleteFBO();
	}

	public void initFBO() {
		GL11.glGenTextures(textures);
		fbo = GL30.glGenFramebuffers();

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);

		// Bind 4 textures to 4 attachments on the fbo
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textures[0], 0);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT1, GL11.GL_TEXTURE_2D, textures[1], 0);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT2, GL11.GL_TEXTURE_2D, textures[2], 0);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT3, GL11.GL_TEXTURE_2D, textures[3], 0);

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	public void deleteFBO() {
		GL30.glDeleteFramebuffers(fbo);
		GL11.glDeleteTextures(textures);
	}

	public void loop() {
		while(!GLFW.glfwWindowShouldClose(handle)) {
			// Draw stuff here

			GLFW.glfwPollEvents();
		}
		destroy();
	}

	public void destroy() {
		GLFW.glfwDestroyWindow(handle);
	}

	public static void main(String[] args) throws Exception {
		if(!GLFW.glfwInit()) throw new Exception("Could not init GLFW");

		new CircleFinder();
	}
}
