package ad_astra_giselle_addon.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import ad_astra_giselle_addon.common.content.oxygen.OxygenChargerUtils;
import ad_astra_giselle_addon.common.registry.AddonEnchantments;
import ad_astra_giselle_addon.common.registry.AddonItems;
import ad_astra_giselle_addon.common.util.TranslationUtils;
import earth.terrarium.ad_astra.AdAstra;
import earth.terrarium.ad_astra.client.screen.GuiUtil;
import earth.terrarium.ad_astra.client.screen.PlayerOverlayScreen;
import earth.terrarium.ad_astra.common.config.AdAstraConfig;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class OxygenCanOverlay
{
	public static final ResourceLocation OXYGEN_TANK_EMPTY_TEXTURE = new ResourceLocation(AdAstra.MOD_ID, "textures/gui/overlay/oxygen_tank_empty.png");
	public static final ResourceLocation OXYGEN_TANK_FULL_TEXTURE = new ResourceLocation(AdAstra.MOD_ID, "textures/gui/overlay/oxygen_tank_full.png");

	public static final String OXYGENCAN_DESCRIPTION_ID = Util.makeDescriptionId("item", AddonItems.OXYGEN_CAN.getId());

	public static void renderHud(PoseStack poseStack, float partialTick)
	{
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;

		if (player.isSpectator() || minecraft.options.renderDebug)
		{
			return;
		}
		else if (PlayerOverlayScreen.shouldRenderOxygen)
		{
			OxygenChargerUtils.getExtractableStoredRatio(player).ifPresent(ratio ->
			{
				Font font = minecraft.font;
				Component component = Component.translatable(OXYGENCAN_DESCRIPTION_ID).append(": ").append(TranslationUtils.formatPercent(ratio)).withStyle(s -> s.withColor(Mth.hsvToRgb((float) (ratio / 3.0F), 1.0F, 1.0F)));
				int textureWidth = 62;
				int textureHeight = 52;
				int width = font.width(component);
				float x = 5 + AdAstraConfig.oxygenBarXOffset + (textureWidth - width) / 2.0F;
				float y = 25 + AdAstraConfig.oxygenBarYOffset + textureHeight + font.lineHeight + 3;
				font.drawShadow(poseStack, component, Math.max(x, 0), y, 0xFFFFFF);
			});
		}
		else if (EnchantmentHelper.getEnchantmentLevel(AddonEnchantments.OXYGEN_PROOF.get(), player) > 0)
		{
			OxygenChargerUtils.getExtractableStoredRatio(player).ifPresent(ratio ->
			{
				renderOxygenCanTank(poseStack, minecraft, ratio);
			});

		}

	}

	public static void renderOxygenCanTank(PoseStack poseStack, Minecraft minecraft, double oxygenRatio)
	{
		poseStack.pushPose();
		poseStack.scale(AdAstraConfig.oxygenBarScale, AdAstraConfig.oxygenBarScale, AdAstraConfig.oxygenBarScale);

		Rect2i rect = PlayerOverlayScreen.getOxygenTankUnscaledRect(minecraft);
		GuiUtil.drawVerticalReverse(poseStack, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), OXYGEN_TANK_EMPTY_TEXTURE, oxygenRatio);
		GuiUtil.drawVertical(poseStack, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), OXYGEN_TANK_FULL_TEXTURE, oxygenRatio);

		// Oxygen text
		Component text = Component.translatable(OXYGENCAN_DESCRIPTION_ID).append(": ").append(TranslationUtils.formatPercent(oxygenRatio)).withStyle(s -> s.withColor(Mth.hsvToRgb((float) (oxygenRatio / 3.0F), 1.0F, 1.0F)));
		int textWidth = minecraft.font.width(text);
		minecraft.font.drawShadow(poseStack, text, (rect.getX() + (rect.getWidth() - textWidth) / 2.0f), rect.getY() + rect.getHeight() + 3, 0xFFFFFF);
		poseStack.popPose();
	}

}
