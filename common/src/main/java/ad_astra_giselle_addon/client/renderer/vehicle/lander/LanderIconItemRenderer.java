package ad_astra_giselle_addon.client.renderer.vehicle.lander;

import com.mojang.blaze3d.vertex.PoseStack;

import earth.terrarium.adastra.client.renderers.entities.vehicles.RocketRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LanderIconItemRenderer extends RocketRenderer.ItemRenderer
{
	public LanderIconItemRenderer(ModelLayerLocation layer, ResourceLocation texture)
	{
		super(layer, texture);
	}

	@Override
	public void onResourceManagerReload(ResourceManager pResourceManager)
	{

	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
	{
		super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
	}

}
