package nukeduck.darthmask;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelMask extends ModelBiped {
	ModelRenderer helmet, visor, maskSlant, maskA, maskB, maskC,
			maskOverlay, buttonLeft, buttonRight;

	public ModelMask() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.bipedBody.showModel = this.bipedHeadwear.showModel
				= this.bipedLeftArm.showModel = this.bipedRightArm.showModel
				= this.bipedLeftLeg.showModel = this.bipedRightLeg.showModel = false;

		helmet = new ModelRenderer(this, 0, 17);
		helmet.addBox(0F, 0F, 0F, 9, 6, 9, 0.01F);
		helmet.setRotationPoint(-4.5F, -8.5F, -4.5F);
		helmet.setTextureSize(64, 32);
		helmet.mirror = true;
		setRotation(helmet, 0F, 0F, 0F);

		visor = new ModelRenderer(this, 28, 14);
		visor.addBox(0F, 0F, 0F, 10, 3, 8);
		visor.setRotationPoint(-5F, -6.5F, -6F);
		visor.setTextureSize(64, 32);
		visor.mirror = true;
		setRotation(visor, 0F, 0F, 0F);

		maskSlant = new ModelRenderer(this, 36, 0);
		maskSlant.addBox(0F, 0F, 0F, 9, 2, 2);
		maskSlant.setRotationPoint(-4.5001F, -3.5F, -6F);
		maskSlant.setTextureSize(64, 32);
		maskSlant.mirror = true;
		setRotation(maskSlant, 0.5576792F, 0F, 0F);

		maskA = new ModelRenderer(this, 36, 4);
		maskA.addBox(0F, 0F, 0F, 6, 2, 2);
		maskA.setRotationPoint(-3F, -3.5F, -6F);
		maskA.setTextureSize(64, 32);
		maskA.mirror = true;
		setRotation(maskA, 0F, 0F, 0F);

		maskB = new ModelRenderer(this, 36, 8);
		maskB.addBox(0F, 0F, -1F, 5, 3, 2);
		maskB.setRotationPoint(-2.5F, -1.7F, -4.5F);
		maskB.setTextureSize(64, 32);
		maskB.mirror = true;
		setRotation(maskB, 0.1487144F, 0F, 0F);

		maskC = new ModelRenderer(this, 50, 10);
		maskC.addBox(0F, 0F, 0F, 4, 2, 1);
		maskC.setRotationPoint(-2F, 1F, -4.9F);
		maskC.setTextureSize(64, 32);
		maskC.mirror = true;
		setRotation(maskC, 0.1858931F, 0F, 0F);

		maskOverlay = new ModelRenderer(this, 52, 4);
		maskOverlay.addBox(0F, 0F, 0F, 3, 5, 1);
		maskOverlay.setRotationPoint(-1.5F, -3F, -5.9F);
		maskOverlay.setTextureSize(64, 32);
		maskOverlay.mirror = true;
		setRotation(maskOverlay, 0.1487144F, 0F, 0F);

		buttonLeft = new ModelRenderer(this, 0, 0);
		buttonLeft.addBox(0F, 0F, 0F, 1, 1, 1);
		buttonLeft.setRotationPoint(0.6F, 1F, -5.6F);
		buttonLeft.setTextureSize(64, 32);
		buttonLeft.mirror = true;
		setRotation(buttonLeft, 0F, 0F, 0F);

		buttonRight = new ModelRenderer(this, 0, 0);
		buttonRight.addBox(0F, 0F, 0F, 1, 1, 1);
		buttonRight.setRotationPoint(-1.6F, 1F, -5.6F);
		buttonRight.setTextureSize(64, 32);
		buttonRight.mirror = true;
		setRotation(buttonRight, 0F, 0F, 0F);
		
		this.bipedHead.addChild(helmet);
		this.bipedHead.addChild(visor);
		this.bipedHead.addChild(maskSlant);
		this.bipedHead.addChild(maskA);
		this.bipedHead.addChild(maskB);
		this.bipedHead.addChild(maskC);
		this.bipedHead.addChild(maskOverlay);
		this.bipedHead.addChild(buttonLeft);
		this.bipedHead.addChild(buttonRight);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
