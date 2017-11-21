package tk.nukeduck.rainbowarmor;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class RainbowArmor extends JavaPlugin {
	private static final Color[] colors = {
		Color.RED, Color.ORANGE, Color.YELLOW, Color.LIME, Color.GREEN, Color.AQUA, Color.BLUE, Color.PURPLE, Color.FUCHSIA
	};
	
	private PluginDescriptionFile description;
	
	public RainbowArmor() {
		super();
		this.description = this.getDescription();
	}
	
	@Override
	public void onEnable() {
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.ENTITY_EQUIPMENT) {
			@Override
			public void onPacketSending(PacketEvent event) {
				if(event.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT) {
					PacketContainer packet = event.getPacket();
					ItemStack armor = packet.getItemModifier().read(0);
					
					if(armor != null) System.out.println(armor.getType().toString());
					
					if(armor.getType() == Material.LEATHER_HELMET) {
						
						packet = packet.deepClone();
						event.setPacket(packet);
						armor = packet.getItemModifier().read(0);
						
						int currentColor = (int) (event.getPlayer().getWorld().getFullTime() % colors.length);
						LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
						meta.setColor(colors[currentColor]);
						meta.setDisplayName(ChatColor.RESET + "Rainbow Cap");
						armor.setItemMeta(meta);
					}
				}
			}
		});
		
		this.getLogger().info(description.getFullName() + " v" + description.getVersion() + " is enabled.");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info(description.getFullName() + " v" + description.getVersion() + " is disabled.");
	}
}