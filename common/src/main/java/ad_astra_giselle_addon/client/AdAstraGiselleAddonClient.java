package ad_astra_giselle_addon.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;

import ad_astra_giselle_addon.client.overlay.OxygenCanOverlay;
import ad_astra_giselle_addon.client.renderer.blockentity.WorkingAreaBlockEntityRenderer;
import ad_astra_giselle_addon.client.renderer.vehicle.lander.LanderIconItemRenderer;
import ad_astra_giselle_addon.client.screen.AutomationNasaWorkbenchScreen;
import ad_astra_giselle_addon.client.screen.FuelLoaderScreen;
import ad_astra_giselle_addon.client.screen.GravityNormalizerScreen;
import ad_astra_giselle_addon.client.screen.RocketSensorScreen;
import ad_astra_giselle_addon.common.AdAstraGiselleAddon;
import ad_astra_giselle_addon.common.enchantment.EnchantmentHelper2;
import ad_astra_giselle_addon.common.item.IClientExtensionItem;
import ad_astra_giselle_addon.common.registry.AddonBlockEntityTypes;
import ad_astra_giselle_addon.common.registry.AddonItems;
import ad_astra_giselle_addon.common.registry.AddonMenuTypes;
import ad_astra_giselle_addon.common.util.TriConsumer;
import earth.terrarium.adastra.client.ClientPlatformUtils;
import earth.terrarium.adastra.client.models.entities.vehicles.LanderModel;
import earth.terrarium.adastra.client.renderers.entities.vehicles.LanderRenderer;
import earth.terrarium.botarium.client.ClientHooks;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AdAstraGiselleAddonClient
{
	private static final Map<IClientExtensionItem, BlockEntityWithoutLevelRenderer> ITEM_RENDERERS = new HashMap<>();

	private AdAstraGiselleAddonClient()
	{

	}

	@Nullable
	public static ConfigScreen getConfigScreen(@Nullable Screen parent)
	{
		ResourcefulConfig config = AdAstraGiselleAddon.config();
		return config != null ? new ConfigScreen(parent, null, config) : null;
	}

	public static void initializeClient()
	{
		registerScreens();
		registerBlockEntityRenderer();

		ITEM_RENDERERS.put(AddonItems.LANDER_ICON.get(), new LanderIconItemRenderer(LanderModel.LAYER, LanderRenderer.TEXTURE));
	}

	public static void registerScreens()
	{
		MenuScreens.register(AddonMenuTypes.FUEL_LOADER.get(), FuelLoaderScreen::new);
		MenuScreens.register(AddonMenuTypes.AUTOMATION_NASA_WORKBENCH.get(), AutomationNasaWorkbenchScreen::new);
		MenuScreens.register(AddonMenuTypes.GRAVITY_NORMALIZER.get(), GravityNormalizerScreen::new);
		MenuScreens.register(AddonMenuTypes.ROCKET_SENSOR.get(), RocketSensorScreen::new);
	}

	public static void onRegisterHud(Consumer<ClientPlatformUtils.RenderHud> register)
	{
		register.accept(OxygenCanOverlay::renderHud);
	}

	public static void registerBlockEntityRenderer()
	{
		ClientHooks.registerBlockEntityRenderers(AddonBlockEntityTypes.FUEL_LOADER.get(), WorkingAreaBlockEntityRenderer::new);
		ClientHooks.registerBlockEntityRenderers(AddonBlockEntityTypes.GRAVITY_NORMALIZER.get(), WorkingAreaBlockEntityRenderer::new);
		ClientHooks.registerBlockEntityRenderers(AddonBlockEntityTypes.ROCKET_SENSOR.get(), WorkingAreaBlockEntityRenderer::new);
	}

	public static void registerItemTooltip(Consumer<TriConsumer<ItemStack, TooltipFlag, List<Component>>> register)
	{
		register.accept(EnchantedBookTooltipHelper::addTooltip);
	}

	public static void registerReloadListeners(BiConsumer<String, PreparableReloadListener> register)
	{
		register.accept("cleardescriptionscache", new ResourceManagerReloadListener()
		{
			@Override
			public void onResourceManagerReload(ResourceManager pResourceManager)
			{
				EnchantmentHelper2.clearDescriptionsCache();
			}
		});
		register.accept("itemrenderers", new ResourceManagerReloadListener()
		{
			@Override
			public void onResourceManagerReload(ResourceManager pResourceManager)
			{
				ITEM_RENDERERS.values().forEach(e -> e.onResourceManagerReload(pResourceManager));
			}
		});
	}

	public static BlockEntityWithoutLevelRenderer getItemRenderer(IClientExtensionItem item)
	{
		return ITEM_RENDERERS.get(item);
	}

	public static Set<Entry<IClientExtensionItem, BlockEntityWithoutLevelRenderer>> getItemRenderers()
	{
		return ITEM_RENDERERS.entrySet();
	}

	public interface BlockEntityRendererRegister
	{
		<T extends BlockEntity> void regsiter(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T> renderer);
	}

}
