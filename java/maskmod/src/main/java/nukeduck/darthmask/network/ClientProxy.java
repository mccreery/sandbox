package nukeduck.darthmask.network;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;
import nukeduck.darthmask.DarthMask;
import nukeduck.darthmask.ModelMask;

public class ClientProxy extends CommonProxy {
	public static final ModelBiped maskModel = new ModelMask();
	public static final String maskTexture = new ResourceLocation(DarthMask.MODID, "textures/models/armor/darth_mask.png").toString();

	@Override
	public int getRenderIndex(String name) {
		return RenderingRegistry.addNewArmourRendererPrefix(name);
	}
}
