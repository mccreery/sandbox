package tk.nukeduck.impossible.client;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.*;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;

import tk.nukeduck.impossible.client.entity.*;
import tk.nukeduck.impossible.client.item.*;
import tk.nukeduck.impossible.client.render.Render;
import tk.nukeduck.impossible.client.render.particle.ParticleText;
import tk.nukeduck.impossible.client.tile.Tile;

public class Main
{
	public static Map gameMap;
	
	/** The size of the window, specified in the program's arguments. **/
	private static int width;
	private static int height;
	
	private static ItemStack[] inventory = new ItemStack[10];
	private static int currentHeldItem = 0;
	
	private static boolean isGamePaused = false;
	
	public static float playerXPosition = 0.0F;
	public static float playerYPosition = 0.0F;
	
	public static float playerXVelocity = 0.0F;
	public static float playerYVelocity = 0.0F;
	
	public static float mapOffsetX = 0;
	public static float mapOffsetY = 0;
	
	public static boolean isDead = false;
	
	public static float playerHealth = 10;
	public static float playerMaxHealth = 10;
	
	/** Tells the renderer which frame of the animation it needs to render. **/
	private static float playerAnimationCounter;
	
	public static float scaleFactor = 4;
	
	static int fps;
	static long lastFps;
	static int displayFps;
	
	public static void main(String[] args)
	{
		width = args.length > 0 ? Integer.parseInt(args[0]) : 1280;
		height = args.length > 1 ? Integer.parseInt(args[1]) : args.length > 0 ? (Integer.parseInt(args[0]) / 16) * 9 : 720;
		if(args.length > 2) scaleFactor = Float.parseFloat(args[2]);
		
		if(!init())
		{
			Logger.getLogger("Impossible").log(Level.SEVERE, "Impossible failed to start.");
		} else
		{
			Logger.getLogger("Impossible").log(Level.INFO, "Impossible started successfully.");
			initGameObjects();
			startGame();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean init()
	{
		DisplayMode displayMode = null;
		try
		{
			if(width == -1 && height == -1)
			{
				System.out.println("Hey!");
				displayMode = Display.getAvailableDisplayModes()[Display.getAvailableDisplayModes().length - 1];
			} else
			{
				displayMode = new DisplayMode(width, height);
			}
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			Display.setDisplayMode(displayMode);
			if(width == -1 && height == -1) 
			{
				Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
				width = Display.getDesktopDisplayMode().getWidth();
				height = Display.getDesktopDisplayMode().getHeight();
			}
			Display.setTitle("Impossible");
			
			Display.sync(60);
			Display.setInitialBackground(0.6F, 0.88F, 1.0F);
			Display.create();
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_BLEND);
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			
			GL11.glOrtho(0, width, height, 0, -1, 1);
			
			g = new Graphics(Display.getWidth(), Display.getHeight());
			g.setAntiAlias(false);
			g.scale(Main.scaleFactor, Main.scaleFactor);
			
		    try {
		        font = new UnicodeFont("5Futurebit.ttf", 4, false, false);
		        font.addAsciiGlyphs();
		        font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		        font.loadGlyphs();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		} catch (LWJGLException e) 
		{
			Logger.getLogger("Impossible").log(Level.SEVERE, "Impossible display failed to start.");
			return false;
		}
		return true;
	}
	
	public static UnicodeFont font;
	
	public static void initGameObjects()
	{
		/** The map we're going to be playing on. **/
		gameMap = new Map(100, 100);
		Item.initItems();
		inventory[0] = new ItemStack(Item.woodenAxe, 1);
	}
	
	/** The main graphics object. **/
	public static Graphics g;
	
	public static Image playerTexture;
	public static Image playerTextureLeft;
	
	/** The tile we're currently standing on. **/
	public static Tile currentTile;
	public static int currentTileHeight = 0;
	
	static boolean right = true;
	static double playerSpeed = 1.2;
	
	static Random random = new Random();
	
	public static void startGame()
	{
		currentTile = Tile.grass;
		try
		{
			playerTexture = new Image("anim2.png");
			playerTexture.setFilter(Image.FILTER_NEAREST);
			playerTextureLeft = new Image("anim2_left.png");
			playerTextureLeft.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e)
		{
			e.printStackTrace();
			playerTexture = null;
			playerTextureLeft = null;
		}
		
		lastFps = getTime();
		lastCount = (int)getTime();
		
		EntityBandit bandit = new EntityBandit(random.nextInt(gameMap.getWidth() * 16), random.nextInt(gameMap.getHeight() * 16), 10, 1.0F);
		if(Entity.getDistance(bandit, playerXPosition, playerYPosition) > 128) Entity.spawnEntity(gameMap, bandit);
		
		while(!Display.isCloseRequested())
		{
		    if(getTime() >= lastCount + 10)
		    {
		    	lastCount += 10;
				if(!isDead && !isGamePaused)
				{
					gameTick();
				}
		    }
		    lastCount++;
	    	
			guiTick();
			renderTick();
		}
		Logger.getLogger("Impossible").log(Level.INFO, "Goodbye!");
		Display.destroy();
	}
	
	private static boolean escapePressed = false;
	
	private static void guiTick()
	{
		if(!escapePressed && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			isGamePaused = !isGamePaused;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			escapePressed = true;
		} else
		{
			escapePressed = false;
		}
	}

	public static boolean isVowel(Character x)
	{
		return x.equals("a") || x.equals("e") || x.equals("i") || x.equals("o") || x.equals("u");
	}
	
    public static long getTime()
    {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    public static void updateFPS(Tile currentTile)
    {
    	if(Keyboard.isKeyDown(Keyboard.KEY_F1))
    	{
        	Render.renderText(5, 5, "Current FPS: " + displayFps, Color.white, font);
        	Render.renderText(5, 12, "Particles: " + gameMap.getParticles().size(), Color.white, font);
        	Render.renderText(5, 19, "Tile: " + currentTile.getName(), Color.white, font);
        	Render.renderText(5, 26, "Player X Velocity: " + playerXVelocity, Color.white, font);
        	Render.renderText(5, 33, "Entities: " + gameMap.getEntities().size(), Color.white, font);
    	}
    	
    	if (getTime() - lastFps > 1000)
    	{
    		displayFps = fps;
    		fps = 0;
    		lastFps += 1000;
        }
    	fps++;
    }
    
    static int lastCount = 0;

	public static Entity lastAttacker = new Entity(0, 0, currentTileHeight, mapOffsetX).setName("Undefined");
    
	public static int attackCountdown;
	
    public static void renderTick()
    {
    	// Render the World
		for(int x = (int) Math.floor(mapOffsetX / 16); x < Math.floor(mapOffsetX / 16) + Display.getWidth() / 16 / scaleFactor + 1; x++)
		{
			for(int y = (int) Math.floor(mapOffsetY / 16) - 1; y < Math.floor(mapOffsetY / 16) + Display.getHeight() / 16 / scaleFactor + 2; y++)
			{
				try
				{
					gameMap.getTileAt(x, y).renderTile(x, y, gameMap);
				} catch(ArrayIndexOutOfBoundsException e)
				{
					// Do absolutely nothing since all that's going to happen is a texture which won't render.
				}
			}
		}
		
		gameMap.particleTick();
		gameMap.renderTick();
    	
		// Render the player
		if(!right)
		{
			g.drawImage(playerTextureLeft, playerXPosition - 16 - mapOffsetX, playerYPosition - 16 - mapOffsetY - currentTileHeight, 16 + playerXPosition - mapOffsetX, 16 + playerYPosition - mapOffsetY - currentTileHeight, 32 * Math.round(playerAnimationCounter), 0, 32 * Math.round(playerAnimationCounter + 1), 32, Color.white);
		} else
		{
			g.drawImage(playerTexture, playerXPosition - 16 - mapOffsetX, playerYPosition - 16 - mapOffsetY - currentTileHeight, 16 + playerXPosition - mapOffsetX, 16 + playerYPosition - mapOffsetY - currentTileHeight, 32 * Math.round(playerAnimationCounter), 0, 32 * Math.round(playerAnimationCounter + 1), 32, Color.white);
		}
		
		if(attackCountdown > 0)
		{
			if(inventory[currentHeldItem] != null)
			{
				Image rotatedImage = inventory[currentHeldItem].getItem().getTexture(right);
				rotatedImage.setRotation(attackCountdown * 16 - 32);
				g.drawImage(rotatedImage, playerXPosition - 12 - mapOffsetX, playerYPosition - 8 - mapOffsetY - currentTileHeight, playerXPosition - mapOffsetX, playerYPosition + 4 - mapOffsetY - currentTileHeight, 0, 0, 16, 16, Color.white);
			}
		}
		
		Render.renderFilledStatusBar(5, Math.round(Display.getHeight() / scaleFactor) - 15, 40, 10, Math.round(playerHealth), Math.round(playerMaxHealth), Color.green, Color.gray);
		Render.renderText(5, Math.round(Display.getHeight() / scaleFactor) - 25, playerHealth + "/" + playerMaxHealth + " HP", Color.white, font);
		
		// Render version number
		Render.renderText(Math.round(Display.getWidth() / scaleFactor / 2) - font.getWidth("Impossible Alpha Version 0.0.0.0.0.0.1") / 2, Math.round(Display.getHeight() / scaleFactor) - 12, "Impossible Alpha Version 0.0.0.0.0.0.1", Color.white, font);
		
		// Render "GAME OVER" if dead
		if(isDead || isGamePaused)
		{
			g.setColor(new Color(0.0F, 0.0F, 0.0F, 0.5F));
			g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
		}
		if(isDead)
		{
			Render.renderText(Math.round(Display.getWidth() / scaleFactor / 2) - font.getWidth("GAME OVER") / 2, Math.round(Display.getHeight() / scaleFactor / 2) - 8, "GAME OVER", Color.red, font);
			if(lastAttacker instanceof EntityBullet)
			{
				Render.renderText(Math.round(Display.getWidth() / scaleFactor / 2) - font.getWidth("You were shot by a " + lastAttacker.creator.getName() + "...") / 2, Math.round(Display.getHeight() / scaleFactor / 2), "You were shot by a " + lastAttacker.creator.getName() + "...", Color.red, font);
			} else
			{
				if(isVowel(lastAttacker.getName().charAt(0)))
				{
					Render.renderText(Math.round(Display.getWidth() / scaleFactor / 2) - font.getWidth("You were slain by an " + lastAttacker.getName() + "...") / 2, Math.round(Display.getHeight() / scaleFactor / 2), "You were slain by an " + lastAttacker.getName() + "...", Color.red, font);
				} else
				{
					Render.renderText(Math.round(Display.getWidth() / scaleFactor / 2) - font.getWidth("You were slain by a " + lastAttacker.getName() + "...") / 2, Math.round(Display.getHeight() / scaleFactor / 2), "You were slain by a " + lastAttacker.getName() + "...", Color.red, font);
				}
			}
		}
    	updateFPS(currentTile);
    	Display.update();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }
    
    public static void playerDamage()
    {
    	ArrayList<Entity> entitiesToDamage = new ArrayList<Entity>();
    	
    	for(int i = 0; i < gameMap.getEntities().size(); i++)
    	{
    		if(right)
    		{
        		if(Entity.getDistance(gameMap.getEntities().get(i), playerXPosition, playerYPosition) < 10 && gameMap.getEntities().get(i).getXPosition() > playerXPosition)
        		{
        			entitiesToDamage.add(gameMap.getEntities().get(i));
        		}
    		} else
    		{
        		if(Entity.getDistance(gameMap.getEntities().get(i), playerXPosition, playerYPosition) < 10 && gameMap.getEntities().get(i).getXPosition() < playerXPosition)
        		{
        			entitiesToDamage.add(gameMap.getEntities().get(i));
        		}
    		}
    	}
    	
		if(inventory[currentHeldItem] != null)
		{
			for(int i = 0; i < entitiesToDamage.size(); i++)
			{
				entitiesToDamage.get(i).setHealth(entitiesToDamage.get(i).getHealth() - inventory[currentHeldItem].getItem().getAttackDamage());
				entitiesToDamage.get(i).xVelocity = right ? 2 : -2;
				Main.gameMap.getParticles().add(new ParticleText(Math.round(entitiesToDamage.get(i).getXPosition()), Math.round(entitiesToDamage.get(i).getYPosition()), 2, -1, 30, Main.g, "-" + Float.toString(inventory[currentHeldItem].getItem().getAttackDamage()), Color.red));
			}
		} else
		{
			for(int i = 0; i < entitiesToDamage.size(); i++)
			{
				entitiesToDamage.get(i).setHealth(entitiesToDamage.get(i).getHealth() - 1);
				entitiesToDamage.get(i).xVelocity = right ? 2 : -2;
				Main.gameMap.getParticles().add(new ParticleText(Math.round(entitiesToDamage.get(i).getXPosition()), Math.round(entitiesToDamage.get(i).getYPosition()), 2, -1, 30, Main.g, "-" + Float.toString(inventory[currentHeldItem].getItem().getAttackDamage()), Color.red));
			}
		}
    }
    
    private static boolean hasHitSinceLastMousePress = false;
    
    public static void gameTick()
    {
    	if(attackCountdown > 0)
    	{
    		attackCountdown -= 1;
    	}
    	
    	if(Mouse.isButtonDown(0) && !hasHitSinceLastMousePress)
    	{
    		playerDamage();
    		attackCountdown = 10;
    	}
    	
    	if(Mouse.isButtonDown(0))
    	{
    		hasHitSinceLastMousePress = true;
    	} else
    	{
    		hasHitSinceLastMousePress = false;
    	}
    	
    	if(random.nextInt(80) == 0)
    	{
    		EntityZombie zombie = new EntityZombie(random.nextInt(gameMap.getWidth() * 16), random.nextInt(gameMap.getHeight() * 16), 10);
    		if(Entity.getDistance(zombie, playerXPosition, playerYPosition) > 128) Entity.spawnEntity(gameMap, zombie);
    	}
    	if(random.nextInt(80) == 0)
    	{
    		EntityBandit bandit = new EntityBandit(random.nextInt(gameMap.getWidth() * 16), random.nextInt(gameMap.getHeight() * 16), 10, 1.0F);
    		if(Entity.getDistance(bandit, playerXPosition, playerYPosition) > 128) Entity.spawnEntity(gameMap, bandit);
    	}
    		
    	if(playerHealth <= 0)
    	{
    		isDead = true;
    		playerHealth = 0;
    	}
    	
		try
		{
			currentTile = gameMap.getTileAt((int)Math.floor(playerXPosition - 8 / 16), (int)Math.floor(playerYPosition - 8 / 16));
			currentTileHeight = gameMap.getTileHeightAt((int)Math.floor(playerXPosition / 16), (int)Math.floor(playerXPosition / 16) + 8);
		} catch(Exception e)
		{
			// Do nothing, this just means the player has walked off the map
		}
		
		if(Mouse.getX() > Display.getWidth() - 32)
		{
			mapOffsetX += 1;
		} else if(Mouse.getX() < 32)
		{
			if(mapOffsetX >= 1) mapOffsetX -= 1;
		}
		if(Mouse.getY() > Display.getHeight() - 32)
		{
			if(mapOffsetY >= 1) mapOffsetY -= 1;
		} else if(Mouse.getY() < 32)
		{
			mapOffsetY += 1;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_D) && playerXVelocity == 0 && playerYVelocity == 0)
		{
			if(random.nextInt(10) == 0)
			{
				gameMap.spawnParticle(currentTile, Math.round(playerXPosition) + random.nextInt(8) - 4 + 8, Math.round(playerYPosition) + random.nextInt(8) - 4 + 8, random(-1, 1), random(-1, 1), g);
			}
			playerAnimationCounter += playerSpeed / 6;
			if(Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				playerXPosition += playerSpeed * currentTile.getWalkingSpeed();
				right = true;
			} else if(Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				playerXPosition -= playerSpeed * currentTile.getWalkingSpeed();
				right = false;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_S))
			{
				playerYPosition += playerSpeed * currentTile.getWalkingSpeed();
			} else if(Keyboard.isKeyDown(Keyboard.KEY_W))
			{
				playerYPosition -= playerSpeed * currentTile.getWalkingSpeed();
			}
		}
		
		playerXPosition += playerXVelocity;
		playerYPosition += playerYVelocity;
		
		if(playerXVelocity >= 0.1)
		{
			playerXVelocity -= 0.1;
		} else if(playerXVelocity <= -0.1)
		{
			playerXVelocity += 0.1;
		} else
		{
			playerXVelocity = 0;
		}
		
		if(playerYVelocity > 0)
		{
			playerYVelocity -= 0.1;
		} else if(playerYVelocity < 0)
		{
			playerYVelocity += 0.1;
		}
		
		gameMap.entityTick();
		
		if(Math.floor(playerAnimationCounter) + 1 >= playerTexture.getWidth() / 32) playerAnimationCounter = 0;
    }
    
    public static double random(double min, double max)
    {
    	double diff = max - min;
    	return min + Math.random( ) * diff;
    }
    
    public static double getAngleBetweenPoints(double x1, double y1, double x2, double y2)
    {
    	double xDifference = x2 - x1;
    	double yDifference = y2 - y1;
    	return Math.toDegrees(Math.atan2(yDifference, xDifference));
    }
}