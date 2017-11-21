package tk.nukeduck.cargame;

import org.lwjgl.util.vector.Vector3f;

import tk.nukeduck.cargame.model.Model;

public class GameObject {
	public Model model = Model.testCube;
	public Vector3f position = new Vector3f(0.0F, 0.0F, 0.0F);
	public float rotation = 0;
	public float rotationY = 0;
	public float health = 1;
	
	public GameObject() {
		this.position = new Vector3f(0.0F, 0.0F, 0.0F);
	}
	
	public GameObject(Model model) {
		this.position = new Vector3f(0.0F, 0.0F, 0.0F);
		this.model = model;
	}
	
	public GameObject(Vector3f position, Model model) {
		this.position = position;
		this.model = model;
	}
	
	public GameObject setModel(Model model) {
		this.model = model;
		return this;
	}
	
	public GameObject setPosition(Vector3f position) {
		this.position = position;
		return this;
	}
	
	public GameObject setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}
	
	public void render() {
		this.model.render(this.position, this.rotation, this.rotationY);
	}
	
	public void tick() {
		this.rotation += 1;
	}
}