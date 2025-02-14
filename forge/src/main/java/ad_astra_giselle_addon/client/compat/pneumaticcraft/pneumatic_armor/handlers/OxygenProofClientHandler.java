package ad_astra_giselle_addon.client.compat.pneumaticcraft.pneumatic_armor.handlers;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import ad_astra_giselle_addon.client.compat.pneumaticcraft.pneumatic_armor.options.OxygenProofOption;
import ad_astra_giselle_addon.common.compat.pneumaticcraft.AddonPNCUpgrades;
import ad_astra_giselle_addon.common.content.oxygen.OxygenStorageUtils;
import ad_astra_giselle_addon.common.registry.AddonItems;
import ad_astra_giselle_addon.common.util.TranslationUtils;
import me.desht.pneumaticcraft.api.client.IGuiAnimatedStat;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IGuiScreen;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IOptionPage;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.StatPanelLayout;
import me.desht.pneumaticcraft.api.pneumatic_armor.IArmorUpgradeHandler;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.api.upgrade.PNCUpgrade;
import me.desht.pneumaticcraft.client.pneumatic_armor.ClientArmorRegistry;
import me.desht.pneumaticcraft.common.config.subconfig.ArmorHUDLayout;
import me.desht.pneumaticcraft.common.pneumatic_armor.CommonArmorHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class OxygenProofClientHandler<T extends IArmorUpgradeHandler<?>> extends AddonSimpleToggleableHandler<T>
{
	private static final StatPanelLayout DEFAULT_STAT_LAYOUT = StatPanelLayout.expandsRight(0.5F, 0.005F);
	private static final ItemStack ICON = new ItemStack(AddonItems.OXYGEN_CAN.get());

	private IGuiAnimatedStat stat;
	private Component oxygenComponent;

	public OxygenProofClientHandler(T commonHandler)
	{
		super(commonHandler);
	}

	@Override
	public IOptionPage getGuiOptionsPage(IGuiScreen screen)
	{
		return new OxygenProofOption<>(screen, this);
	}

	@Override
	public void tickClient(ICommonArmorHandler armorHandler, boolean isEnabled)
	{
		super.tickClient(armorHandler, isEnabled);

		if (isEnabled && this.stat.isStatOpen())
		{
			Player player = armorHandler.getPlayer();
			double ratio = OxygenStorageUtils.getExtractableStoredRatio(player).orElse(0.0D);
			this.oxygenComponent = TranslationUtils.formatPercent(ratio);
		}
		else
		{
			this.oxygenComponent = null;
		}

	}

	@Override
	public void render2D(GuiGraphics graphics, float partialTicks, boolean armorPieceHasPressure)
	{
		super.render2D(graphics, partialTicks, armorPieceHasPressure);

		if (this.stat != null && this.oxygenComponent != null && !ArmorHUDLayout.INSTANCE.getLayoutFor(this.getCommonHandler().getID(), this.getDefaultStatLayout()).hidden())
		{
			Minecraft instance = Minecraft.getInstance();
			Font fr = instance.font;
			int xl = this.stat.getBaseX() + 4;
			int yt = this.stat.getBaseY() + 16;

			if (this.stat.isLeftSided())
			{
				xl -= stat.getStatWidth();
			}

			int iconWidth = 16;
			int iconX = xl;
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			graphics.renderItem(ICON, iconX, yt);
			RenderSystem.disableBlend();

			int componentX = iconX + iconWidth + 2;
			int componentY = yt + 4;
			graphics.drawString(fr, this.oxygenComponent, componentX, componentY, 0xFFFFFF, true);
		}

	}

	@Override
	public IGuiAnimatedStat getAnimatedStat()
	{
		if (this.stat == null)
		{
			PNCUpgrade upgrade = AddonPNCUpgrades.OXYGEN_PROOF.get();
			CommonArmorHandler handler = CommonArmorHandler.getHandlerForPlayer();
			int tier = Math.max(1, handler.getUpgradeCount(this.getCommonHandler().getEquipmentSlot(), upgrade));
			ItemStack stack = new ItemStack(upgrade.getItem(tier));

			this.stat = ClientArmorRegistry.getInstance().makeHUDStatPanel(Component.translatable(this.getCommonHandler().getTranslationKey()), stack, this);
			this.stat.setMinimumContractedDimensions(0, 0);
			this.stat.setMinimumExpandedDimensions(60, 30);
		}

		return this.stat;
	}

	@Override
	public void onResolutionChanged()
	{
		this.stat = null;
	}

	@Override
	public StatPanelLayout getDefaultStatLayout()
	{
		return DEFAULT_STAT_LAYOUT;
	}

}
