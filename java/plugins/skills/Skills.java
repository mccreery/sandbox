package tk.nukeduck.Skills;

import java.io.*;
import java.util.*;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

public class Skills extends JavaPlugin implements Listener {
	HashMap<String, int[]> skills;
	HashMap<String, int[]> mana;
	HashMap<String, Material> currentSpells = new HashMap<String, Material>();
	
	HashMap<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();
	String[] skillNames = new String[] {"Melee", "Archery", "Sorcery", "Healing", "Agility"};
	
	static String configPath = "plugins" + File.separator + "Skills" + File.separator;
	
	private ItemStack spellBook;
	private ItemStack wand;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		File f = new File(configPath + "skills.txt");
		File f1 = new File(configPath + "mana.txt");
		if(f.exists()) {
			skills = load(f.getPath());
			mana = load(f1.getPath());
		} else {
			skills = new HashMap<String, int[]>();
			mana = new HashMap<String, int[]>();
		}
		
		spellBook = new ItemStack(Material.ENCHANTED_BOOK);
		spellBook.setDurability((short) 1);
		
		ItemMeta itemMeta = spellBook.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + "Book o' Spells");
        
        itemMeta.setLore(Arrays.asList("Holds up to five magical spells."));
        spellBook.setItemMeta(itemMeta);
		
		ShapedRecipe recipe = new ShapedRecipe(spellBook);
		
		recipe.shape("SLL", "DBE", "SLL");
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('B', Material.BOOK);
		recipe.setIngredient('E', Material.EYE_OF_ENDER);
		
		getServer().addRecipe(recipe);
		
		wand = new ItemStack(Material.BLAZE_ROD);
		wand.setDurability((short) 1);
		
		ItemMeta itemMeta2 = wand.getItemMeta();
        itemMeta2.setDisplayName(ChatColor.BLUE + "Sorcerer's Wand");
        
        itemMeta2.setLore(Arrays.asList("Right-click to cast the current spell."));
        wand.setItemMeta(itemMeta2);
		
		ShapedRecipe recipe2 = new ShapedRecipe(wand);
		
		recipe2.shape("EO ", "OD ", "  B");
		recipe2.setIngredient('E', Material.EYE_OF_ENDER);
		recipe2.setIngredient('O', Material.OBSIDIAN);
		recipe2.setIngredient('D', Material.DIAMOND);
		recipe2.setIngredient('B', Material.BLAZE_ROD);
		
		getServer().addRecipe(recipe2);
		
		spellNames.put(Material.BLAZE_POWDER, "Flame Vortex");
		spellCosts.put(Material.BLAZE_POWDER, 10);
		spellNames.put(Material.FEATHER, "Leap");
		spellCosts.put(Material.FEATHER, 5);
		spellNames.put(Material.ICE, "Ice Blitz");
		spellCosts.put(Material.ICE, 20);
		spellNames.put(Material.TNT, "Nuclear Blast");
		spellCosts.put(Material.TNT, 50);
		spellNames.put(Material.PORTAL, "Void Warp");
		spellCosts.put(Material.PORTAL, 35);
		spellNames.put(Material.POTION, "Curse");
		spellCosts.put(Material.POTION, 40);
		
		spellItems = new ItemStack[spellNames.size()];
		for(int i = 0; i < spellNames.size(); i++) {
			spellItems[i] = new ItemStack((Material) (spellNames.keySet().toArray()[i]));
			ItemMeta meta = spellItems[i].getItemMeta();
			meta.setDisplayName(spellNames.get(spellItems[i].getType()));
			List<String> lore = new ArrayList<String>();
			lore.add(spellCosts.get(spellItems[i].getType()) + " mana");
			meta.setLore(lore);
			spellItems[i].setItemMeta(meta);
		}
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    public void run() {
		    	for(Player p : getServer().getOnlinePlayers()) {
		    		if(random.nextBoolean() && mana.get(p.getName())[0] < mana.get(p.getName())[1]) updateMana(p, mana.get(p.getName())[0] + 1, true);
		    	}
		    }
		}, 0, 5);
	}
	
	HashMap<Material, Integer> spellCosts = new HashMap<Material, Integer>();
	
	ItemStack[] spellItems;
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getItem() != null && e.getItem().equals(spellBook) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Inventory inv = Bukkit.createInventory(null, 9, "Book o' Spells");
			inv.addItem(spellItems);
			
			for(ItemStack i : inv.getContents()) {
				if(i != null) {
					//if(currentSpells.get(e.getPlayer().getName()) == null) currentSpells.put(e.getPlayer().getName(), Material.BLAZE_POWDER);
					if(currentSpells.get(e.getPlayer().getName()) == i.getType()) {
						i = addGlow(i);
					}
				}
			}
			
			e.getPlayer().openInventory(inv);
			e.setCancelled(true);
		}
		
		if(e.getItem() != null && e.getItem().equals(wand) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			spell(e.getPlayer());
			e.setCancelled(true);
		}
	}
	
	Random random = new Random();
	
	public void spell(Player player) {
		if(currentSpells.get(player.getName()) == null) {
			player.sendMessage(ChatColor.DARK_RED + "You don't have a spell selected.");
		} else if(mana.get(player.getName())[0] >= spellCosts.get(currentSpells.get(player.getName()))) {
			switch(currentSpells.get(player.getName())) {
				case FEATHER:
					player.setVelocity(player.getLocation().getDirection().multiply(2F));
					player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.5F, random.nextFloat() * 0.5F + 0.5F);
					player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 0);
					break;
				case BLAZE_POWDER:
					Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
					SmallFireball f = (SmallFireball) player.getWorld().spawnEntity(loc, EntityType.SMALL_FIREBALL);
					f.setShooter(player);
					player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
					break;
				case POTION:
					ThrownPotion p = (ThrownPotion) player.getWorld().spawnEntity(player.getTargetBlock(null, 20).getLocation(), EntityType.SPLASH_POTION);
					p.setShooter(player);
					
					ItemStack potion = new ItemStack(Material.POTION);
					PotionMeta meta = ((PotionMeta) potion.getItemMeta());
					meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 200, 1), true);
					
					potion.setItemMeta(meta);
					p.setItem(potion);
					break;
				case TNT:
					for(int i = -3; i < random.nextInt(3); i++) {
						TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
						tnt.setFuseTicks(100);
						tnt.setVelocity(player.getLocation().getDirection().add(new Vector((random.nextFloat() - 0.5F) / 4, (random.nextFloat() - 0.5F) / 4, (random.nextFloat() - 0.5F) / 4)));
					}
					break;
				case ICE:
					try {
						ParticleEffects.BUBBLE.sendToPlayer(player, player.getEyeLocation().add(player.getLocation().getDirection()), 0, 0, 0, 0, 5);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case PORTAL:
					Location a = player.getLocation();
					Vector b = player.getLocation().getDirection().multiply(0.1);
					
					break;
				default:
					player.sendMessage(ChatColor.DARK_RED + "This spell doesn't work yet.");
			}
			if(player.getGameMode() != GameMode.CREATIVE) updateMana(player, mana.get(player.getName())[0] - spellCosts.get(currentSpells.get(player.getName())), true);
		} else {
			player.sendMessage(ChatColor.GRAY + "You need at least " + ChatColor.RED + spellCosts.get(currentSpells.get(player.getName())) + ChatColor.GRAY + " mana to cast this spell.");
		}
	}
	
	public static ItemStack addGlow(ItemStack item){ 
		item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
		return item;
	}
	
	public static Map<Material, String> spellNames = new HashMap<Material, String>();
	
	@EventHandler
	public void onItemInInvClick(InventoryClickEvent e) {
		for(Material m : spellNames.keySet().toArray(new Material[]{})) {
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() == m && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() == spellNames.get(m)) {
				currentSpells.put(e.getWhoClicked().getName(), m);
				((Player) e.getWhoClicked()).sendMessage(ChatColor.LIGHT_PURPLE + spellNames.get(m) + " selected.");
				
				e.getWhoClicked().closeInventory();
				e.setCancelled(true);
			}
		}
	}
	
	public void onDisable() {
		save(skills, configPath + "skills.txt");
		save(mana, configPath + "mana.txt");
	}
	
	@EventHandler
	public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
		if(new Random().nextInt(3) == 0) {
			if(event.getDamager() instanceof Player && event.getDamage() > 0) {
				Player player = (Player) event.getDamager();
				Score score = scoreboards.get(player.getName()).getObjective("skills").getScore(this.getServer().getOfflinePlayer(ChatColor.BLUE + "" + ChatColor.BOLD + skillNames[0] + ":"));
				score.setScore(score.getScore() + 1);
				
				skills.get(player.getName())[0] = score.getScore();
			} else if(event.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) event.getDamager();
				if(arrow.getShooter().getType() == EntityType.PLAYER) {
					Player player = (Player) (arrow.getShooter());
					
					Score score = scoreboards.get(player.getName()).getObjective("skills").getScore(this.getServer().getOfflinePlayer(ChatColor.BLUE + "" + ChatColor.BOLD + skillNames[1] + ":"));
					score.setScore(score.getScore() + 1);
					
					skills.get(player.getName())[1] = score.getScore();
				}
			}
		}
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if(e.getItem() != null) {
			Player player = e.getPlayer();
			Score score = scoreboards.get(player.getName()).getObjective("skills").getScore(this.getServer().getOfflinePlayer(ChatColor.BLUE + "" + ChatColor.BOLD + skillNames[3] + ":"));
			int amount = 0;
			switch(e.getItem().getType()) {
				// Cooked foods
				case COOKED_CHICKEN:
				case BAKED_POTATO:
					amount = 1;
					break;
				case COOKED_BEEF:
				case GRILLED_PORK:
					amount = 3;
					break;
				case COOKED_FISH:
					amount = 2;
					break;
				
				// Raw foods
				case RAW_CHICKEN:
				case RAW_BEEF:
				case PORK:
				case RAW_FISH:
					amount = -2;
					break;
				
				// Raw potatoes and carrots do nothing
				case POTATO:
				case CARROT:
					break;
				
				// Others
				case APPLE:
				case MELON:
					amount = 1;
					break;
				case BREAD:
				case MUSHROOM_SOUP:
				case PUMPKIN_PIE:
					amount = 2;
					break;
				case GOLDEN_APPLE:
				case GOLDEN_CARROT:
					amount = 5;
					break;
				case SPIDER_EYE:
					amount = -5;
					break;
				case POISONOUS_POTATO:
				case ROTTEN_FLESH:
					amount = -2;
					break;
				
				// Cookies!
				case COOKIE:
					amount = 4;
					break;
					
				default:
					break;
			}
			
			score.setScore(score.getScore() + amount);
			if(amount != 0) player.sendMessage(ChatColor.GRAY + "Healing " + (amount > 0 ? ChatColor.GREEN + "increased" : ChatColor.RED + "decreased") + ChatColor.GRAY + " by " + ChatColor.LIGHT_PURPLE + Math.abs(amount) + ChatColor.GRAY + " by eating " + ChatColor.BOLD + ChatColor.WHITE + e.getItem().getType().toString());
			
			skills.get(player.getName())[3] = score.getScore();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!skills.containsKey(event.getPlayer().getName())) {
			skills.put(event.getPlayer().getName(), new int[] {0, 0, 0, 0, 0});
		}
		
		if(!mana.containsKey(event.getPlayer().getName())) {
			mana.put(event.getPlayer().getName(), new int[] {0, 100});
		}
		
		Scoreboard s = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective o = s.registerNewObjective("skills", "dummy");
		o.setDisplayName(ChatColor.GREEN + "Skills");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(int i = 0; i < skillNames.length; i++) {
			o.getScore(this.getServer().getOfflinePlayer(ChatColor.BLUE + "" + ChatColor.BOLD + skillNames[i] + ":")).setScore(skills.get(event.getPlayer().getName())[i]);
		}
		
		o.getScore(this.getServer().getOfflinePlayer(ChatColor.GREEN + "Mana:")).setScore(-1);
		o.getScore(this.getServer().getOfflinePlayer(generateBar(mana.get(event.getPlayer().getName())[0], mana.get(event.getPlayer().getName())[1]))).setScore(-mana.get(event.getPlayer().getName())[0]);;
		
		event.getPlayer().setScoreboard(s);
		scoreboards.put(event.getPlayer().getName(), s);
	}
	
	public void updateMana(Player p, int manaAmount, boolean display) {
		Scoreboard s = scoreboards.get(p.getName());
		if(display) s.resetScores(this.getServer().getOfflinePlayer(generateBar(mana.get(p.getName())[0], mana.get(p.getName())[1])));
		mana.put(p.getName(), new int[] {manaAmount, mana.get(p.getName())[1]});
		if(display) s.getObjective("skills").getScore(this.getServer().getOfflinePlayer(generateBar(mana.get(p.getName())[0], mana.get(p.getName())[1]))).setScore(-manaAmount);
	}
	
	public String generateBar(int value, int maxValue) {
		int bar = Math.min(Math.round(((float) value / (float) maxValue) * 10), 10);
		return (bar > 6 ? ChatColor.GREEN : bar > 3 ? ChatColor.GOLD : ChatColor.DARK_RED) + "██████████".substring(0, bar) + ChatColor.DARK_GRAY + "██████████".substring(0, 10 - bar);
	}
	
	public void save(HashMap<String, int[]> map, String path) {
		try {
			File f = new File(path);
			if(!f.exists()) f.createNewFile();
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(map);
			oos.flush();
			oos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, int[]> load(String path) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			Object result = ois.readObject();
			ois.close();
			//you can feel free to cast result to HashMap<String, Integer> if you know there's that HashMap in the file
			return (HashMap<String, int[]>) result;
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Skills is null, dawg.");
		return null;
	}
}