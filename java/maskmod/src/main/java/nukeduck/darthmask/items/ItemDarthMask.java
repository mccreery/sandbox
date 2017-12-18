package nukeduck.darthmask.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import nukeduck.darthmask.DarthMask;
import nukeduck.darthmask.ModelMask;
import nukeduck.darthmask.network.ClientProxy;

public class ItemDarthMask extends ItemArmor {
	public ItemDarthMask(int renderIndex) {
		super(DarthMask.MASK, 2, 0);
		this.canRepair = true;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return ClientProxy.maskTexture;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		return ClientProxy.maskModel;
	}
}
