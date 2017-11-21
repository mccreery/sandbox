package tk.nukeduck.impossible.client.item;

public class ItemStack
{
	private Item item;
	private int amount;
	
	public ItemStack(Item item, int amount)
	{
		this.item = item;
		this.amount = amount;
	}
	
	public Item getItem()
	{
		return this.item;
	}
	
	public int amount()
	{
		return this.amount;
	}
}
