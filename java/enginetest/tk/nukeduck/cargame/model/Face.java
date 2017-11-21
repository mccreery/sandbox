package tk.nukeduck.cargame.model;

import org.lwjgl.util.vector.Vector3f;

public class Face {
	public Vector3f vertex = new Vector3f();
	public Vector3f normal = new Vector3f();
	public Vector3f color = new Vector3f();

	public Face(Vector3f vertex, Vector3f normal) {
		this.vertex = vertex;
		this.normal = normal;
	}

	public Face(Vector3f vertex, Vector3f normal, Vector3f color) {
		this.vertex = vertex;
		this.normal = normal;
		this.color = color;
	}
}
