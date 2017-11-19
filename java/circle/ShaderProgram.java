import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderProgram {
	private final int program;

	private static int bound;
	public void bind() {
		GL20.glUseProgram(bound = program);
	}
	public static void unbind() {
		GL20.glUseProgram(bound = 0);
	}

	/** @param vert The path to the vertex shader file
	 * @param frag The path to the fragment shader file */
	public ShaderProgram(String vert, String frag) throws IOException {
		vert = getSource(vert);
		frag = getSource(frag);

		int vertShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		GL20.glShaderSource(vertShader, vert);
		GL20.glShaderSource(fragShader, frag);

		GL20.glCompileShader(vertShader);
		GL20.glCompileShader(fragShader);

		if(GL20.glGetShaderi(vertShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE || GL20.glGetShaderi(fragShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println(GL20.glGetShaderInfoLog(vertShader));
			System.err.println(GL20.glGetShaderInfoLog(fragShader));
			throw new IOException("Invalid shader code");
		}

		program = GL20.glCreateProgram();
		GL20.glAttachShader(program, vertShader);
		GL20.glAttachShader(program, fragShader);

		GL20.glLinkProgram(program);

		if(GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.err.println(GL20.glGetProgramInfoLog(program));
			throw new IOException("Could not link shader");
		}
	}

	public static String getSource(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		StringBuilder builder = new StringBuilder();

		String line;
		while((line = reader.readLine()) != null) {
			builder.append(line);
		}

		reader.close();
		return builder.toString();
	}
}
