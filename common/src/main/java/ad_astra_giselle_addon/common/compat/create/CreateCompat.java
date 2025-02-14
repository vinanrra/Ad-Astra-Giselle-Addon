package ad_astra_giselle_addon.common.compat.create;

import java.util.List;

import com.mojang.brigadier.builder.ArgumentBuilder;

import ad_astra_giselle_addon.common.compat.CompatibleMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;

public class CreateCompat extends CompatibleMod
{
	public static final String MOD_ID = "create";

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

	@Override
	public String getModId()
	{
		return MOD_ID;
	}

	@Override
	protected void onLoad()
	{

	}

	@Override
	public void collectEquipCommands(List<ArgumentBuilder<CommandSourceStack, ?>> list)
	{
		super.collectEquipCommands(list);
		list.add(Commands.literal("diving_helmet").executes(CreateCommand::diving_helmet));
		list.add(Commands.literal("netherite_diving_helmet").executes(CreateCommand::netherite_diving_helmet));
	}

}
