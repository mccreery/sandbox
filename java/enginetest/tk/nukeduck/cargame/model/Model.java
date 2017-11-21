package tk.nukeduck.cargame.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Model {
	public static final Model testCube = ObjectLoader.loadModel("src/cube.mdl").setTexture("src/main.png");
	
	private int[][] faces;
	public int renderer;
	public Image texture;
	public float scale = 1.0F;
	
	public Model(int[][] faces) {
		this.faces = faces;
		this.updateVBO();
	}
	
	public Model setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	public Model setTexture(String texture) {
		try {
			this.texture = new Image(texture);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public int vertic;
	
	public void updateVBO() {
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(faces.length * 15); // vertex = 3, texture = 2
		vertic = faces.length;
		
		System.out.println("Faces:" + faces.length);
		
		for (int[] face : faces) {
			vertexData.put(new float[] {
				face[0], face[1], face[2], (float) face[3] / 8F, (float) face[4] / 8F,		// Vertex 1
				face[5], face[6], face[7], (float) face[8] / 8F, (float) face[9] / 8F,		// Vertex 2
				face[10], face[11], face[12], (float) face[13] / 8F, (float) face[14] / 8F	// Vertex 3
			});
		}
		vertexData.flip();
		
		this.renderer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.renderer);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void render(Vector3f position, float rotation, float rotationY) {
		glPushMatrix(); {
			this.texture.bind();
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			glTranslatef(position.x, position.y, position.z);
			glRotatef(rotation, 0, 1, 0);
			glRotatef(rotationY, 0, 0, 1);
			
			glScalef(scale, scale, scale);
			
			/*glBegin(GL_TRIANGLES);
			glVertex3f(0.0F, 0.0F, 0.0F);
			glVertex3f(0.0F, 1.0F, 0.0F);
			glVertex3f(0.0F, 0.0F, 1.0F);
			glEnd();*/
			
			glBindBuffer(GL_ARRAY_BUFFER, this.renderer);
	        
	        glEnableClientState(GL_VERTEX_ARRAY);
	        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
	        
	        glVertexPointer(3, GL_FLOAT, 20, 0);
	        glTexCoordPointer(2, GL_FLOAT, 20, 12);
	        
	        glDrawArrays(GL_TRIANGLES, 0, vertic * 3);
	        
	        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	        glDisableClientState(GL_VERTEX_ARRAY);
	        
	        glBindBuffer(GL_ARRAY_BUFFER, 0);
	        glBindTexture(GL_TEXTURE_2D, 0);
		}
		glPopMatrix();
	}
}
