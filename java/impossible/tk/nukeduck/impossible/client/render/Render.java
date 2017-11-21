package tk.nukeduck.impossible.client.render;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

import tk.nukeduck.impossible.client.Main;

public class Render
{
    public static void renderStatusBar(int x, int y, int width, int height, int value, int maxValue, Color color)
    {
    	Main.g.setColor(new Color(color.r - 0.5F, color.g - 0.5F, color.b - 0.5F, color.a - 0.5F));
    	Main.g.drawRect(x + 1, y + 1, width, height);
    	Main.g.fillRect(x + 1, y + 1, Math.round(value * width / maxValue), height);
    	
    	Main.g.setColor(color);
    	Main.g.drawRect(x, y, width, height);
    	Main.g.fillRect(x, y, Math.round(value * width / maxValue), height);
    }
    
    public static void renderFilledStatusBar(int x, int y, int width, int height, int value, int maxValue, Color color, Color backColor)
    {
    	Main.g.setColor(new Color(backColor.r - 0.5F, backColor.g - 0.5F, backColor.b - 0.5F, backColor.a - 0.5F));
    	Main.g.fillRect(x + 1, y + 1, width, height);
    	
    	Main.g.setColor(new Color(color.r - 0.5F, color.g - 0.5F, color.b - 0.5F, color.a - 0.5F));
    	Main.g.fillRect(x + 1, y + 1, Math.round(value * width / maxValue), height);
    	
    	
    	Main.g.setColor(backColor);
    	Main.g.fillRect(x, y, width, height);
    	
    	Main.g.setColor(color);
    	Main.g.fillRect(x, y, Math.round(value * width / maxValue), height);
    }
    
	public static void renderText(int x, int y, String text, Color color, UnicodeFont font)
    {
		// Render shadow
		font.drawString(x + 1, y + 1, text, new Color(color.r - 0.5F, color.g - 0.5F, color.b - 0.5F, color.a - 0.5F));
	    // Render text
	    font.drawString(x, y, text, color);
    }
}
