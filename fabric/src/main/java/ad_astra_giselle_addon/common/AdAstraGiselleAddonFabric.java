package ad_astra_giselle_addon.common;

import java.util.Collections;

import ad_astra_giselle_addon.common.config.AddonConfigs;
import ad_astra_giselle_addon.common.delegate.CreativeModeTabBuilder;
import ad_astra_giselle_addon.common.delegate.DelegateLivingHelper;
import ad_astra_giselle_addon.common.delegate.DelegateProvider;
import ad_astra_giselle_addon.common.delegate.DelegateRegistryHelper;
import ad_astra_giselle_addon.common.delegate.FabricRegisterHelper;
import ad_astra_giselle_addon.common.delegate.PlatformCommonDelegate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.world.item.CreativeModeTab;

public class AdAstraGiselleAddonFabric implements ModInitializer, PlatformCommonDelegate
{
	@Override
	public void onInitialize()
	{
		AdAstraGiselleAddon.initializeCommon(this);
		AdAstraGiselleAddon.registerConfig(AddonConfigs.class);
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, selection) -> AdAstraGiselleAddon.registerCommand(dispatcher::register));
	}

	@Override
	public DelegateRegistryHelper getRegistryHelper()
	{
		return FabricRegisterHelper.INSTANCE;
	}

	@Override
	public CreativeModeTab createCreativeModeTab(CreativeModeTabBuilder builder)
	{
		return FabricItemGroupBuilder.create(builder.id()).icon(builder.icon()).appendItems(builder::appendItems).build();
	}

	@Override
	public DelegateLivingHelper getLivingHelper()
	{
		return living -> Collections.emptyList();
	}

	@Override
	public DelegateProvider getAddonHelper()
	{
		return () -> Collections.emptyList();
	}

}
