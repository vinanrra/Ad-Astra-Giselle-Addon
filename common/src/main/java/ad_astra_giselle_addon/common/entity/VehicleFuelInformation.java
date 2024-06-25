package ad_astra_giselle_addon.common.entity;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public record VehicleFuelInformation(FluidContainer fuelTank, TagKey<Fluid> fuelTag)
{

}
