package tk.nukeduck.SpecialMobSpawn;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;

public class SpecialMobSpawn extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	Random random = new Random();
	
	public Material[] helmets = new Material[] {
		Material.LEATHER_HELMET, 
		Material.IRON_HELMET, 
		Material.GOLD_HELMET, 
		Material.DIAMOND_HELMET, 
		Material.SPONGE, 
		Material.MELON_BLOCK, 
		Material.PUMPKIN
	};
	
	public Material[] chestplates = new Material[] {
		Material.LEATHER_CHESTPLATE, 
		Material.IRON_CHESTPLATE, 
		Material.GOLD_CHESTPLATE, 
		Material.DIAMOND_CHESTPLATE
	};
	
	public Material[] leggings = new Material[] {
			Material.LEATHER_LEGGINGS, 
			Material.IRON_LEGGINGS, 
			Material.GOLD_LEGGINGS, 
			Material.DIAMOND_LEGGINGS
	};
	
	public Material[] boots = new Material[] {
			Material.LEATHER_BOOTS, 
			Material.IRON_BOOTS, 
			Material.GOLD_BOOTS, 
			Material.DIAMOND_BOOTS
	};
	
	@EventHandler
	public void hit(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager().getType() == EntityType.ZOMBIE && random.nextFloat() > 0.9F) {
			if(random.nextBoolean()) ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1));
			else if(random.nextBoolean()) ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 60, 1));
			else ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 1));
		}
	}
	
    @EventHandler
    public void spawn(CreatureSpawnEvent event) {
    	EntityType type = event.getEntityType();
    	
    	if(random.nextInt(3) == 0 && (type == EntityType.CREEPER || type == EntityType.CAVE_SPIDER || type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.SPIDER || type == EntityType.ENDERMAN)) {
	    	if(random.nextInt(100) == 0) {
	    		// Create stack of the mob
	    		Entity[] stack = new Entity[random.nextInt(5) + 2];
	    		stack[0] = event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), type);
	    		for(int i = 1; i < stack.length; i++) {
	    			stack[i] = event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), type);
	    			stack[i - 1].setPassenger(stack[i]);
	    		}
	    	} else {
	    		if(random.nextFloat() >= 0.95F) {
	    			if(type == EntityType.SKELETON) {
	    				// Make skeleton bat jockey
	    				Skeleton entity = (Skeleton) (event.getEntity());
	    				
	    				if(random.nextBoolean()) entity.setSkeletonType(SkeletonType.WITHER);
	    				
	    				Bat bat = (Bat) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.BAT);
	    				bat.setPassenger(entity);
	    			} else if(random.nextFloat() > 0.75F) {
	    				// Random blaze spawns
		    			event.setCancelled(true);
		    			event.getEntity().getWorld().spawnEntity(event.getLocation(), EntityType.BLAZE);
	    			}
	    		} else if(random.nextFloat() >= 0.3F) {
		    		if(random.nextInt(20) == 0) {
		    			// Create slime stack
		    			event.setCancelled(true);
		    			
		    			Slime small = (Slime) event.getEntity().getWorld().spawnEntity(event.getLocation(), EntityType.SLIME);
		    			Slime medium = (Slime) event.getEntity().getWorld().spawnEntity(event.getLocation(), EntityType.SLIME);
		    			Slime large = (Slime) event.getEntity().getWorld().spawnEntity(event.getLocation(), EntityType.SLIME);
		    			small.setSize(1);
		    			medium.setSize(2);
		    			large.setSize(3);
		    			
		    			large.setPassenger(medium);
		    			medium.setPassenger(small);
		    		} else {
		    			// Custom armour
		    			if(random.nextBoolean()) {
		    				if(type == EntityType.ZOMBIE || type == EntityType.SKELETON) {
		    					// Helmet
		    					if(random.nextBoolean()) {
		    						int index = random.nextInt(helmets.length);
		    						ItemStack helmet = new ItemStack(helmets[index], 1);
		    						helmet.setDurability((short) (random.nextInt(helmet.getType().getMaxDurability() + 1) - 1));
		    						if(random.nextBoolean() && index < 4) helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, random.nextInt(4) + 1);
		    						event.getEntity().getEquipment().setHelmet(helmet);
		    						event.getEntity().getEquipment().setHelmetDropChance(0.2F);
		    					}
		    					// Chestplate
		    					if(random.nextBoolean()) {
		    						ItemStack chest = new ItemStack(chestplates[random.nextInt(chestplates.length)], 1);
		    						chest.setDurability((short) (random.nextInt(chest.getType().getMaxDurability() + 1) - 1));
		    						if(random.nextBoolean()) chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, random.nextInt(4) + 1);
		    						if(random.nextBoolean()) chest.addEnchantment(Enchantment.THORNS, random.nextInt(3) + 1);
		    						event.getEntity().getEquipment().setChestplate(chest);
		    						event.getEntity().getEquipment().setChestplateDropChance(0.05F);
		    					}
		    					// Leggings
		    					if(random.nextBoolean()) {
		    						ItemStack legs = new ItemStack(leggings[random.nextInt(leggings.length)], 1);
		    						legs.setDurability((short) (random.nextInt(legs.getType().getMaxDurability() + 1) - 1));
		    						if(random.nextBoolean()) legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, random.nextInt(4) + 1);
		    						event.getEntity().getEquipment().setLeggings(legs);
		    						event.getEntity().getEquipment().setLeggingsDropChance(0.1F);
		    					}
		    					// Boots
		    					if(random.nextBoolean()) {
		    						ItemStack boot = new ItemStack(boots[random.nextInt(boots.length)], 1);
		    						boot.setDurability((short) (random.nextInt(boot.getType().getMaxDurability() + 1) - 1));
		    						if(random.nextBoolean()) boot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, random.nextInt(4) + 1);
		    						event.getEntity().getEquipment().setBoots(boot);
		    						event.getEntity().getEquipment().setBootsDropChance(0.2F);
		    					}
		    				}
		    			} else {
		    				if(random.nextBoolean()) {
		    					// Jockey
		        				switch(type) {
			    					case ZOMBIE:
			    						// Small slime jockey
			    						Slime rider = (Slime) event.getEntity().getWorld().spawnEntity(event.getLocation(), EntityType.SLIME);
			    						rider.setSize(2);
			    						event.getEntity().setPassenger(rider);
			    					default:
			    						break;
		        				}
		    				} else {
		    					// Custom potion effects
								switch(random.nextInt(4)) {
									case 0: event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 32767, random.nextInt(2) + 1, false));
									case 1: event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 32767, random.nextInt(3) + 1, false));
									case 2: event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 32767, random.nextInt(3) + 1, false));
									case 3: event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 32767, random.nextInt(2) + 1, false));
								}
		    				}
		    			}
		    		}
	    		}
		    }
  		}
    }
}