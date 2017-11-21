package tk.nukeduck.impossible.client.gui;

import org.newdawn.slick.Color;

import tk.nukeduck.impossible.client.Main;
import tk.nukeduck.impossible.client.render.Render;

public class GuiButton
{
	private int x = 5;
	private int y = 5;
	private int width = 100;
	private int height = 20;
	private String text = "Undefined";
	
	public GuiButton(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public GuiButton(int x, int y, int width, int height, String text)
	{
		this(x, y, width, height);
		this.text = text;
	}
	
	public GuiButton setText(String text)
	{
		this.text = text;
		return this;
	}
	
	public void checkForInput()
	{
		
	}
	
	public void render()
	{
		Main.g.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
		Main.g.fillRect(this.x, this.y, this.width, this.height);
		
		Main.g.drawRect(this.x, this.y, this.width, this.height);
		
		Render.renderText(Math.round(this.x / Main.scaleFactor) + (this.width / 2) - Main.font.getWidth(text) / 2 + 4, Math.round(this.y / Main.scaleFactor) + (this.height / 2), text, Color.white, Main.font);
	}
}
