package ad_astra_giselle_addon.common.compat.create;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.simibubi.create.content.equipment.armor.BacktankUtil;

import ad_astra_giselle_addon.common.command.AddonCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class CreateCommand
{
	public static int diving_helmet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		CommandSourceStack source = context.getSource();
		ServerPlayer player = source.getPlayerOrException();

		player.setItemSlot(EquipmentSlot.HEAD, AddonCommand.Equip.makeFullWithEnchantments(CreateCompat.rl("copper_diving_helmet")));
		player.setItemSlot(EquipmentSlot.CHEST, fillAir(AddonCommand.Equip.makeFullWithEnchantments(CreateCompat.rl("copper_backtank"))));
		player.setItemSlot(EquipmentSlot.FEET, AddonCommand.Equip.makeFullWithEnchantments(CreateCompat.rl("copper_diving_boots")));

		return AddonCommand.sendEquipedMessage(source);
	}

	public static int netherite_diving_helmet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		CommandSourceStack source = context.getSource();
		ServerPlayer player = source.getPlayerOrException();

		player.setItemSlot(EquipmentSlot.HEAD, AddonCommand.Equip.makeFullWithEnchantments(CreateCompat.rl("netherite_diving_helmet")));
		player.setItemSlot(EquipmentSlot.CHEST, fillAir(AddonCommand.Equip.makeFullWithEnchantments(CreateCompat.rl("netherite_backtank"))));
		player.setItemSlot(EquipmentSlot.FEET, AddonCommand.Equip.makeFullWithEnchantments(CreateCompat.rl("netherite_diving_boots")));

		return AddonCommand.sendEquipedMessage(source);
	}

	public static ItemStack fillAir(ItemStack stack)
	{
		var maxAir = BacktankUtil.maxAir(stack);
		stack.getOrCreateTag().putFloat("Air", maxAir);
		return stack;
	}

	private CreateCommand()
	{

	}

}
