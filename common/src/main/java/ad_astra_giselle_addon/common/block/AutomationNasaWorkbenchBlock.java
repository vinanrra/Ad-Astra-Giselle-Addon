package ad_astra_giselle_addon.common.block;

import java.util.List;

import ad_astra_giselle_addon.common.AdAstraGiselleAddon;
import ad_astra_giselle_addon.common.block.entity.AutomationNasaWorkbenchBlockEntity;
import ad_astra_giselle_addon.common.registry.AddonBlocks;
import earth.terrarium.ad_astra.common.block.machine.AbstractMachineBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AutomationNasaWorkbenchBlock extends AbstractMachineBlock
{
	public static final String TOOLTIP_KEY = AdAstraGiselleAddon.tl("block", AddonBlocks.AUTOMATION_NASA_WORKBENCH.getId(), "tooltip");

	public AutomationNasaWorkbenchBlock(Properties settings)
	{
		super(settings);
	}

	@Override
	public void appendHoverText(ItemStack item, BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(item, level, tooltip, flag);

		tooltip.add(Component.translatable("item.ad_astra.nasa_workbench.tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
		tooltip.add(Component.translatable(TOOLTIP_KEY).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new AutomationNasaWorkbenchBlockEntity(pos, state);
	}

}
