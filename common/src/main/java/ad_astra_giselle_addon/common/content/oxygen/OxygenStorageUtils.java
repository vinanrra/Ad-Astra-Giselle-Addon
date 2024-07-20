package ad_astra_giselle_addon.common.content.oxygen;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import ad_astra_giselle_addon.common.compat.CompatibleManager;
import ad_astra_giselle_addon.common.compat.create.BacktankOxygenStorage;
import ad_astra_giselle_addon.common.entity.LivingHelper;
import ad_astra_giselle_addon.common.item.ItemStackReference;
import earth.terrarium.adastra.api.systems.TemperatureApi;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class OxygenStorageUtils
{
	public static OptionalDouble getExtractableStoredRatio(LivingEntity living)
	{
		List<ItemStackReference> items = LivingHelper.getInventoryItems(living);
		long stored = 0L;
		long capacity = 0L;
		Level level = living.level();
		BlockPos pos = living.blockPosition();
		boolean isCold = TemperatureApi.API.isCold(level, pos);
		boolean isHot = TemperatureApi.API.isHot(level, pos);

		for (ItemStackReference item : items)
		{
			IOxygenStorage oxygenStorage = OxygenStorageUtils.get(item);

			if (oxygenStorage != null && oxygenStorage.canUse(isCold, isHot))
			{
				stored += oxygenStorage.getOxygenAmount();
				capacity += oxygenStorage.getOxygenCapacity();
			}

		}

		if (capacity == 0L)
		{
			return OptionalDouble.empty();
		}
		else
		{
			return OptionalDouble.of((double) stored / capacity);
		}

	}

	@Nullable
	public static IOxygenStorage firstExtractable(LivingEntity living, long extracting)
	{
		return streamExtractable(living, extracting).findFirst().orElse(null);
	}

	@Nullable
	public static Stream<IOxygenStorage> streamExtractable(LivingEntity living, long extracting)
	{
		Level level = living.level();
		BlockPos pos = living.blockPosition();
		boolean isCold = TemperatureApi.API.isCold(level, pos);
		boolean isHot = TemperatureApi.API.isHot(level, pos);

		return LivingHelper.getInventoryItems(living).stream().map(OxygenStorageUtils::get).filter(oxygenStorage ->
		{
			if (oxygenStorage != null && oxygenStorage.canUse(isCold, isHot))
			{
				long extract = oxygenStorage.extractOxygen(living, extracting, true);

				if (extract >= extracting)
				{
					return true;
				}

			}

			return false;
		});
	}

	@Nullable
	public static IOxygenStorage get(ItemStackHolder item)
	{
		if (item.getStack().getItem() instanceof IOxygenStorageItem oxygenStorageItem)
		{
			return oxygenStorageItem.getOxygenStorage(item);
		}

		if (CompatibleManager.Create.isLoaded())
		{
			var storage = BacktankOxygenStorage.getOxygenStroage(item);

			if (storage != null)
			{
				return storage;
			}

		}

		return null;
	}

	private OxygenStorageUtils()
	{

	}

}
