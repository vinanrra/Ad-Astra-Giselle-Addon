package ad_astra_giselle_addon.client.mixin;

import org.spongepowered.asm.mixin.Mixin;

import ad_astra_giselle_addon.client.renderer.blockentity.WorkingAreaBlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.extensions.IBlockEntityRendererExtension;

@Mixin(value = WorkingAreaBlockEntityRenderer.class, remap = false)
public abstract class WorkingAreaBlockEntityRendererMixin<T extends BlockEntity> implements IBlockEntityRendererExtension<T>
{
	@Override
	public AABB getRenderBoundingBox(T blockEntity)
	{
		return IBlockEntityRendererExtension.INFINITE_EXTENT_AABB;
	}

}
