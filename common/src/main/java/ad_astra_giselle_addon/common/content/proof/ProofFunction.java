package ad_astra_giselle_addon.common.content.proof;

import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface ProofFunction
{
	int provide(Entity entity);
}
