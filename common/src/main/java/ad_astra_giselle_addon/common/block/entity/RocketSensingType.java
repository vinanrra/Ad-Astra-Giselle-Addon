package ad_astra_giselle_addon.common.block.entity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ad_astra_giselle_addon.common.AdAstraGiselleAddon;
import ad_astra_giselle_addon.common.registry.AddonItems;
import ad_astra_giselle_addon.common.util.RedstoneUtils;
import earth.terrarium.adastra.common.entities.vehicles.Lander;
import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import earth.terrarium.adastra.common.entities.vehicles.Vehicle;
import earth.terrarium.adastra.common.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.redstone.Redstone;

public enum RocketSensingType implements IRocketSensingType
{
	DISABLED(AdAstraGiselleAddon.rl("disabled"))
		{
			@Override
			public boolean test(Vehicle vehicle)
			{
				return vehicle instanceof Rocket;
			}

			@Override
			public int getAnalogSignal(Vehicle vehicle)
			{
				return Redstone.SIGNAL_NONE;
			}

			@Override
			public ItemStack getDisplayIcon()
			{
				return new ItemStack(Items.GUNPOWDER);
			}

		},
	FOUND(AdAstraGiselleAddon.rl("found"))
		{
			@Override
			public boolean test(Vehicle vehicle)
			{
				return vehicle instanceof Rocket;
			}

			@Override
			public int getAnalogSignal(Vehicle vehicle)
			{
				return Redstone.SIGNAL_MAX;
			}

			@Override
			public ItemStack getDisplayIcon()
			{
				return new ItemStack(Items.REDSTONE);
			}

		},
	FLYING_UP(AdAstraGiselleAddon.rl("flying_up"))
		{
			@Override
			public boolean test(Vehicle vehicle)
			{
				return vehicle instanceof Rocket;
			}

			@Override
			public int getAnalogSignal(Vehicle vehicle)
			{
				Rocket rocket = (Rocket) vehicle;
				return RedstoneUtils.onOff(rocket.hasLaunched());
			}

			@Override
			public ItemStack getDisplayIcon()
			{
				return new ItemStack(ModItems.TIER_1_ROCKET.get());
			}

		},
	FUEL_LOADED(AdAstraGiselleAddon.rl("fuel_loaded"))
		{
			@Override
			public boolean test(Vehicle vehicle)
			{
				return vehicle instanceof Rocket;
			}

			@Override
			public int getAnalogSignal(Vehicle vehicle)
			{
				Rocket rocket = (Rocket) vehicle;
				return RedstoneUtils.onOff(rocket.hasEnoughFuel());
			}

			@Override
			public ItemStack getDisplayIcon()
			{
				return new ItemStack(ModItems.FUEL_BUCKET.get());
			}

		},
	LAUNCH_COUNTDOWN(AdAstraGiselleAddon.rl("launch_countdown"), 2)
		{
			@Override
			public boolean test(Vehicle vehicle)
			{
				return vehicle instanceof Rocket;
			}

			@Override
			public int getAnalogSignal(Vehicle vehicle)
			{
				Rocket rocket = (Rocket) vehicle;
				int max = Rocket.COUNTDOWN_LENGTH;
				double ratio = (max - rocket.launchTicks()) / (double) max;
				return rocket.isLaunching() ? RedstoneUtils.range(ratio) : Redstone.SIGNAL_MIN;
			}

			@Override
			public ItemStack getDisplayIcon()
			{
				return new ItemStack(ModItems.LAUNCH_PAD.get());
			}

		},
	LANDER_FOUND(AdAstraGiselleAddon.rl("lander_found"), 1)
		{
			@Override
			public boolean test(Vehicle vehicle)
			{
				return vehicle instanceof Lander;
			}

			@Override
			public int getAnalogSignal(Vehicle vehicle)
			{
				return Redstone.SIGNAL_MAX;
			}

			@Override
			public ItemStack getDisplayIcon()
			{
				return new ItemStack(AddonItems.LANDER_ICON.get());
			}

		},
	// EOL
	;

	private final ResourceLocation name;
	private final Component displayName;
	private final List<Component> tooltip;

	private RocketSensingType(ResourceLocation name)
	{
		this(name, 1);
	}

	private RocketSensingType(ResourceLocation name, int tooltipLines)
	{
		this.name = name;
		this.displayName = Component.translatable(AdAstraGiselleAddon.tl(LANGUGE_CATEGORY_ROCKET_SENSING_TYPE, name));
		Object[] tooltipParams = new Object[]{Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX};

		if (tooltipLines == 1)
		{
			this.tooltip = Collections.singletonList(Component.translatable(AdAstraGiselleAddon.tl(LANGUGE_CATEGORY_ROCKET_SENSING_TYPE, name, "desc"), tooltipParams));
		}
		else
		{
			this.tooltip = IntStream.range(0, tooltipLines).mapToObj(i -> Component.translatable(AdAstraGiselleAddon.tl(LANGUGE_CATEGORY_ROCKET_SENSING_TYPE, name, "desc" + (i + 1)), tooltipParams)).collect(Collectors.toUnmodifiableList());
		}

	}

	@Override
	public ResourceLocation getName()
	{
		return this.name;
	}

	@Override
	public Component getDisplayName()
	{
		return this.displayName;
	}

	@Override
	public List<Component> getTooltip()
	{
		return this.tooltip;
	}

	@Override
	public abstract boolean test(Vehicle vehicle);

	@Override
	public abstract int getAnalogSignal(Vehicle vehicle);
}
