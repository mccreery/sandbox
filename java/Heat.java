import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;

public class Main {
	public static double getDistance(Vector2f a, Vector2f b) {
		return Math.sqrt(Math.pow(b.y - a.y, 2) + Math.pow(b.x - a.x, 2));
	}
	
	private static int brushSize = 200;
	private static boolean shouldMove = true;
	
	private static ArrayList<Vector2f> points2 = new ArrayList<Vector2f>();
	private static ArrayList <Float> pointHeat = new ArrayList<Float>();
	
	public static void main(String[] args) {
		for(int x = 0; x <= 1000; x += 5) {
			for(int y = 0; y <= 1000; y += 5) {
				points2.add(new Vector2f(x, y));
				pointHeat.add(0.0F);
			}
		}
		
		try {
			Display.setDisplayMode(new DisplayMode(1000, 1000));
			Display.setTitle("Heat");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1000, 1000, 0, 0, 1);
		glMatrixMode(GL_MODELVIEW);
		glDisable(GL_DEPTH_TEST);
		
		glPointSize(4);
		
		while(!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				shouldMove = false;
			} else {
				shouldMove = true;
			}
			
			// Grow
			if(Mouse.isButtonDown(0)) {
				for(int i = 0; i < points2.size(); i++) {
					if(getDistance(points2.get(i), new Vector2f(Mouse.getX(), 1000 - Mouse.getY())) < brushSize) {
						float dist = (float)getDistance(points2.get(i), new Vector2f(Mouse.getX(), 1000 - Mouse.getY()));
						
						if(shouldMove) {
							points2.set(i, new Vector2f(
									(float)((points2.get(i).x - Mouse.getX()) * (1 + (0.01 * ((brushSize - dist) / brushSize)) ) ) + Mouse.getX(), 
									(float)((points2.get(i).y - (1000 - Mouse.getY())) * (1 + (0.01 * ((brushSize - dist) / brushSize)) ) ) + (1000 - Mouse.getY())
							));
						}
						
						if(pointHeat.get(i) < 0.5F) pointHeat.set(i, pointHeat.get(i) + (0.01F * ((brushSize - dist) / brushSize)));
					}
				}
			}
			
			// Shrink
			if(Mouse.isButtonDown(1)) {
				for(int i = 0; i < points2.size(); i++) {
					if(getDistance(points2.get(i), new Vector2f(Mouse.getX(), 1000 - Mouse.getY())) < brushSize) {
						float dist = (float)getDistance(points2.get(i), new Vector2f(Mouse.getX(), 1000 - Mouse.getY()));
						
						if(shouldMove) {
							points2.set(i, new Vector2f(
									(float)((points2.get(i).x - Mouse.getX()) * (1 - (0.01 * ((brushSize - dist) / brushSize)) ) ) + Mouse.getX(), 
									(float)((points2.get(i).y - (1000 - Mouse.getY())) * (1 - (0.01 * ((brushSize - dist) / brushSize)) ) ) + (1000 - Mouse.getY())
							));
						}
						
						if(pointHeat.get(i) < 0.9F) pointHeat.set(i, pointHeat.get(i) + (0.1F * ((brushSize - dist) / brushSize)));
					}
				}
			}
			
			for(int i = 0; i < points2.size(); i++) {
				if(pointHeat.get(i) > 0) {
					pointHeat.set(i, pointHeat.get(i) - 0.001F);
				}
			}
			
			// Reset
			if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
				points2.clear();
				for(int x = 0; x <= 1000; x += 5) {
					for(int y = 0; y <= 1000; y += 5) {
						points2.add(new Vector2f(x, y));
					}
				}
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				brushSize --;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				brushSize ++;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				for(int i = 0; i < points2.size(); i++) {
					g.setColor(new Color(1, 0, 0, pointHeat.get(i)));
					g.drawLine((int)points2.get(i).x / 5, (int)points2.get(i).y / 5, (int)points2.get(i).x / 5, (int)points2.get(i).y / 5);
				}
				
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy 'at' HH-mm-ss");
				Date today = Calendar.getInstance().getTime();
				String date = df.format(today);
				
				File outputfile = new File(date + ".png");
				try {
					ImageIO.write(image, "png", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.dispose();
			}
			
			glBegin(GL_POINTS); {
				for(int i = 0; i < points2.size(); i++) {
					glColor3f(0.1F + pointHeat.get(i), 0.1F, 0.1F);
					glVertex2f(points2.get(i).x, points2.get(i).y);
				}
			}
			glEnd();
			
			Display.update();
		}
	}
}