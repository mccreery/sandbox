package tk.nukeduck.cargame;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Graphics;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.GLU;

import static org.lwjgl.util.glu.GLU.*;
import tk.nukeduck.cargame.model.*;

public class Main {
	public static Vector3f cameraOffset = new Vector3f(0.0F, 0.0F, -5.0F);
	public static float rotationYaw = 0;
	public static float rotationPitch = 0;
	
	public static float bobAmount;
	
	public static float cameraOffsetYVel = 0.0F;
	
	static int fps;
	static long lastFps;
	static int displayFps;
	
	static int lastCount = 0;
	
	public static void main(String[] args) {
		initDisplay();
		initOpenGL();
		initGameItems();
		gameLoop();
		Display.destroy();
	}
	
	public static ArrayList<GameObject> items = new ArrayList<GameObject>();
	
	public static void initGameItems() {
		items.add(new GameObject().setModel(Model.testCube));
	}
	
	public static void initDisplay() {
		try {
			//Display.setFullscreen(true);
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.setTitle("Dildo Simulator 2015");
			Display.setInitialBackground(1.0F, 1.0F, 1.0F);
			Display.create(new PixelFormat(0, 8, 0, 8));
			Mouse.setGrabbed(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initOpenGL() {
		glEnable(GL_TEXTURE_2D);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(90, (float) Display.getWidth() / (float) Display.getHeight(), 0.1F,
				64.0F);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	// moves the camera forward relative to its current rotation (yaw)
	public static void walkForward(float distance) {
		cameraOffset.x -= distance * Math.sin(Math.toRadians(rotationYaw));
		cameraOffset.z += distance * Math.cos(Math.toRadians(rotationYaw));
	}
	
	// moves the camera backward relative to its current rotation (yaw)
	public static void walkBackwards(float distance) {
		cameraOffset.x += distance * Math.sin(Math.toRadians(rotationYaw));
		cameraOffset.z -= distance * Math.cos(Math.toRadians(rotationYaw));
	}
	
	// strafes the camera left relitive to its current rotation (yaw)
	public static void strafeLeft(float distance) {
		cameraOffset.x -= distance * Math.sin(Math.toRadians(rotationYaw - 90));
		cameraOffset.z += distance * Math.cos(Math.toRadians(rotationYaw - 90));
	}
	
	// strafes the camera right relitive to its current rotation (yaw)
	public static void strafeRight(float distance) {
		cameraOffset.x -= distance * Math.sin(Math.toRadians(rotationYaw + 90));
		cameraOffset.z += distance * Math.cos(Math.toRadians(rotationYaw + 90));
	}
	
	public static long lastTime;

	public static void gameLoop() {
		lastFps = getTime();
		lastTime = getTime();
		lastCount = (int) getTime();

		while (!Display.isCloseRequested()
				&& !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			renderTick();
			if (getTime() >= lastTime + 10) {
				inputTick();
				lastTime = getTime();
				for (GameObject p : pendingItems) {
					items.add(p);
				}
				pendingItems.clear();

				for (int i = 0; i < items.size(); i++) {
					items.get(i).tick();
					if (items.get(i).health == 0) {
						items.remove(i);
					}
				}
			}
		}
	}
	
	public static ArrayList<GameObject> pendingItems = new ArrayList<GameObject>();
	
	public static float vibrateSpeed = 0.1F;
	
	private static void inputTick() {
		//System.out.println("X: " + cameraOffset.x + ", Y: " + cameraOffset.y + ", Z: " + cameraOffset.z + ", Pitch: " + rotationPitch + ", Yaw: " + rotationYaw);
		if (Keyboard.isKeyDown(Keyboard.KEY_W)
				|| Keyboard.isKeyDown(Keyboard.KEY_A)
				|| Keyboard.isKeyDown(Keyboard.KEY_S)
				|| Keyboard.isKeyDown(Keyboard.KEY_D)
				|| Math.round(Math.sin(bobAmount)) != 0) {
			bobAmount += 0.3;
		} else if (Math.round(Math.sin(bobAmount)) == 0) {
			bobAmount = 0;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			walkForward(0.05F);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			walkBackwards(0.05F);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			strafeLeft(0.05F);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			strafeRight(0.05F);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && cameraOffset.y >= 0) {
			cameraOffsetYVel = -0.15F;
		}
		
		cameraOffset.y += cameraOffsetYVel;
		if (cameraOffset.y < 0) {
			cameraOffsetYVel += 0.005;
		} else {
			cameraOffset.y = 0;
		}
		
		// Mouse movement
		
		mouseAccelerationX += (float) Mouse.getDX() * mouseSpeed;
		mouseAccelerationY -= (float) Mouse.getDY() * mouseSpeed;
		
		mouseAccelerationX *= mouseDampening;
		mouseAccelerationY *= mouseDampening;

		rotationYaw += mouseAccelerationX;
		rotationPitch += mouseAccelerationY;
		
		if(scopeViewAmount > 0.0F && Mouse.isButtonDown(1)) {
			scopeViewAmount -= 0.01F;
		} else if(scopeViewAmount < 1.0F && !Mouse.isButtonDown(1)) {
			scopeViewAmount += 0.01F;
		}
		
		if (rotationPitch > 90) {
			rotationPitch = 90;
		} else if (rotationPitch < -90) {
			rotationPitch = -90;
		}
	}
	
	public static float smoothstep(float x) {return x * x * (3 - 2 * x);}
	public static float smootherstep(float x) {return x * x * x * (6 * x * x - 15 * x + 10);}
	
	public static float scopeViewAmount = 0.0F;
	
	public static float mouseAccelerationX = 0.0F, mouseAccelerationY = 0.0F;
	public static float mouseDampening = 0.2F;
	public static float mouseSpeed = 0.4F;
	
	private static void renderItems() {
		for (GameObject p : items) {
			p.render();
		}
	}
	
	public static int textureSize = 1024;
	
	private static void renderTick() {
		glLoadIdentity();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glRotatef(rotationPitch, 1, 0, 0);
		glRotatef(rotationYaw, 0, 1, 0);
		glTranslatef(cameraOffset.x, cameraOffset.y - 0.5F, cameraOffset.z);
		
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		{
			renderItems();
		}
		glPopAttrib();
		
		renderOverlay();
		
		//updateFPS();
		Display.update();
	}

	public static void renderOverlay() {
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		GLU.gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_COLOR_MATERIAL);
		
		glPushMatrix();
		glLoadIdentity();

		glDisable(GL_DEPTH_TEST);
		// glEnable(GL_BLEND);
		// glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		
		glBegin(GL_QUADS);
		{
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			glVertex2f(Display.getWidth() / 2 - 8, Display.getHeight() / 2 - 1);
			glVertex2f(Display.getWidth() / 2 - 8, Display.getHeight() / 2 + 1);
			glVertex2f(Display.getWidth() / 2 + 8, Display.getHeight() / 2 + 1);
			glVertex2f(Display.getWidth() / 2 + 8, Display.getHeight() / 2 - 1);
			
			glVertex2f(Display.getWidth() / 2 - 1, Display.getHeight() / 2 - 8);
			glVertex2f(Display.getWidth() / 2 - 1, Display.getHeight() / 2 + 8);
			glVertex2f(Display.getWidth() / 2 + 1, Display.getHeight() / 2 + 8);
			glVertex2f(Display.getWidth() / 2 + 1, Display.getHeight() / 2 - 8);
		}
		glEnd();
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_LIGHTING);
		
		glEnable(GL_DEPTH_TEST);
		
		glPopMatrix();
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
	}
	
	static Graphics graphics = new Graphics();

	public static void updateFPS() {
		if (getTime() - lastFps > 1000) {
			displayFps = fps;
			fps = 0;
			lastFps += 1000;
		}
		fps++;
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private static FloatBuffer asFloatBuffer(float[] values) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
}
