package ad_astra_giselle_addon.client.compat;

import ad_astra_giselle_addon.common.AdAstraGiselleAddon;
import ad_astra_giselle_addon.common.block.entity.FuelLoaderBlockEntity;
import ad_astra_giselle_addon.common.registry.ObjectRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

public class RecipeHelper
{
	public static final String JEI_CATEGORY = "jei.category";
	public static final String JEI_INFO = "jei.info";
	public static final String JEI_TOOLTIP = "jei.tooltip";

	public static ResourceLocation createUid(ResourceLocation key)
	{
		return new ResourceLocation(key.getNamespace(), JEI_CATEGORY + "." + key.getPath());
	}

	public static Component getCategoryTitle(ResourceLocation key)
	{
		return Component.translatable(JEI_CATEGORY + "." + key.getNamespace() + "." + key.getPath());
	}

	public static Component getInfoTitle(ItemLike item)
	{
		return item.asItem().getDescription();
	}

	public static Component getInfoBody(ItemLike item, Object[] objects)
	{
		return Component.translatable(AdAstraGiselleAddon.tl(JEI_INFO, ObjectRegistry.get(Registries.ITEM).getId(item.asItem())), objects);
	}

	public class FuelLoader
	{
		public static final ResourceLocation BACKGROUND_LOCATION = AdAstraGiselleAddon.rl("textures/jei/fuel_loader.png");
		public static final int BACKGROUND_WIDTH = 144;
		public static final int BACKGROUND_HEIGHT = 56;
		public static final int TANK_LEFT = 53;
		public static final int TANK_TOP = 10;
		public static final int TANK_WIDTH = 12;
		public static final int TANK_HEIGHT = 32;

		public static boolean testFluid(Fluid fluid)
		{
			return FuelLoaderBlockEntity.isFuel(fluid) && fluid.isSource(fluid.defaultFluidState());
		}

	}

	private RecipeHelper()
	{

	}

}
