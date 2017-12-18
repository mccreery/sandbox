package nukeduck.darthmask;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.EnumHelper;
import nukeduck.darthmask.items.ItemDarthMask;
import nukeduck.darthmask.network.CommonProxy;

@Mod(modid=DarthMask.MODID, name="Darth Revan's Mask", version="1.0")
public class DarthMask {
	public static final String MODID = "darthmask";

	@Instance(MODID)
	public static DarthMask INSTANCE;
	@SidedProxy(clientSide="nukeduck.darthmask.network.ClientProxy", serverSide="nukeduck.darthmask.network.CommonProxy")
	public static CommonProxy PROXY;

	public static final ArmorMaterial MASK = EnumHelper.addArmorMaterial("MASK", 50, new int[] {3, 0, 0, 0}, 15);
	public static Item mask;

	@EventHandler
	public void init(FMLInitializationEvent e) {
		this.mask = new ItemDarthMask(PROXY.getRenderIndex("mask"));
		GameRegistry.registerItem(mask.setUnlocalizedName("darthMask").setTextureName(new ResourceLocation(MODID, "darth_mask").toString()), "darth_mask");

		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(mask), 1, 1, 1));
	}
}
