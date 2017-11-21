package nukeduck.crawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import nukeduck.crawler.graphics.Texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureLoader {
	public static final int loadTexture(ResourcePath resource, Texture texture) {
		return loadTexture(resource.getFile(), texture);
	}
	public static final int loadTexture(File file, Texture texture) {
		int id = 0;
		try {
			FileInputStream in = new FileInputStream(file);
			PNGDecoder decoder = new PNGDecoder(in);
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);

			buffer.flip();
			texture.processImage(buffer, decoder.getWidth(), decoder.getHeight());
			
			in.close();

			id = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

			// Setup texture scaling filtering
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}
}
