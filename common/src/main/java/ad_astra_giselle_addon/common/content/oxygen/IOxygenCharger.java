package ad_astra_giselle_addon.common.content.oxygen;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ad_astra_giselle_addon.common.fluid.FluidHooks2;
import ad_astra_giselle_addon.common.fluid.FluidPredicates;
import ad_astra_giselle_addon.common.fluid.UniveralFluidHandler;
import net.minecraft.world.entity.LivingEntity;

public interface IOxygenCharger extends IOxygenStorage
{
	public default List<IChargeMode> getAvailableChargeModes()
	{
		return Arrays.asList(ChargeMode.values());
	}

	@NotNull
	IChargeMode getChargeMode();

	void setChargeMode(@Nullable IChargeMode mode);

	long getTransferAmount();

	UniveralFluidHandler getFluidHandler();

	@Override
	default long extractOxygen(@Nullable LivingEntity entity, long amount, boolean simulate)
	{
		UniveralFluidHandler fluidHandler = this.getFluidHandler();
		return FluidHooks2.extractFluid(fluidHandler, FluidPredicates::isOxygen, amount, simulate).getFluidAmount();
	}

	@Override
	default long getOxygenAmount()
	{
		UniveralFluidHandler fluidHandler = this.getFluidHandler();
		int size = fluidHandler.getTankAmount();
		long amount = 0L;

		for (int i = 0; i < size; i++)
		{
			amount += fluidHandler.getFluidInTank(i).getFluidAmount();
		}

		return amount;
	}

	@Override
	default long getOxygenCapacity()
	{
		UniveralFluidHandler fluidHandler = this.getFluidHandler();
		int size = fluidHandler.getTankAmount();
		long capacity = 0L;

		for (int i = 0; i < size; i++)
		{
			capacity += fluidHandler.getTankCapacity(i);
		}

		return capacity;
	}

}
