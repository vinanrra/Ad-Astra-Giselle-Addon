package ad_astra_giselle_addon.client.renderer.vehicle.lander;

import com.mojang.blaze3d.vertex.PoseStack;

import earth.terrarium.ad_astra.client.renderer.entity.vehicle.VehicleRenderer;
import earth.terrarium.ad_astra.client.renderer.entity.vehicle.lander.LanderModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;

public class LanderIconItemRenderer extends BlockEntityWithoutLevelRenderer
{
	private final ModelLayerLocation model;
	private final ResourceLocation texture;
	private EntityModel<?> bakedModel;

	public LanderIconItemRenderer(ModelLayerLocation model, ResourceLocation texture)
	{
		super(null, null);

		this.model = model;
		this.texture = texture;
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
	{
		EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
		this.bakedModel = new LanderModel(entityModelSet.bakeLayer(this.model));
	}

	@Override
	public void renderByItem(ItemStack stack, TransformType mode, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
	{
		VehicleRenderer.renderItem(this.bakedModel, this.texture, null, poseStack, buffer, packedLight, packedOverlay);
	}

}
