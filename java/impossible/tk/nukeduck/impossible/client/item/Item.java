package tk.nukeduck.impossible.client.item;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Item
{
	public static Item woodenAxe;
	
	private int maxAmount = 50;
	private float attackDamage = 1;
	private String name = "Unknown";
	private Image texture = null;
	private Image flippedTexture = null;
	
	public static void initItems()
	{
		try
		{
			woodenAxe = new Item("Wooden Axe").setAttackDamage(2.5F).setMaxAmount(0).setTexture(new Image("axe.png"));
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	public Item(String name)
	{
		this.name = name;
	}
	
	public Item setTexture(Image texture)
	{
		this.texture = texture;
		texture.setFilter(Image.FILTER_NEAREST);
		
		try
		{
			this.flippedTexture = new Image(texture.getResourceReference(), true);
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
		flippedTexture.setFilter(Image.FILTER_NEAREST);
		return this;
	}
	
	public Image getTexture(boolean flipped)
	{
		return flipped ? this.flippedTexture : this.texture;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Item setAttackDamage(float amount)
	{
		this.attackDamage = amount;
		return this;
	}
	
	public float getAttackDamage()
	{
		return this.attackDamage;
	}
	
	public Item setMaxAmount(int amount)
	{
		this.maxAmount = amount;
		return this;
	}
	
	public int getMaxAmount()
	{
		return this.maxAmount;
	}
}
