package ad_astra_giselle_addon.common.compat.create;

import org.apache.commons.lang3.Range;
import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.equipment.armor.BacktankUtil;

import ad_astra_giselle_addon.common.content.oxygen.IOxygenStorage;
import ad_astra_giselle_addon.common.content.proof.ProofAbstractUtils;
import ad_astra_giselle_addon.common.fluid.FluidHooks2;
import earth.terrarium.ad_astra.common.item.armor.SpaceSuit;
import earth.terrarium.ad_astra.common.registry.ModItems;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.item.ItemStackHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;

public class BacktankOxygenStorage implements IOxygenStorage
{
	public static IOxygenStorage getOxygenStroage(ItemStackHolder holder)
	{
		if (holder.getStack().getItem() instanceof BacktankItem item)
		{
			return new BacktankOxygenStorage(holder, item.getMaterial());
		}

		return null;
	}

	private final ItemStackHolder holder;
	private final ArmorMaterial material;

	public BacktankOxygenStorage(ItemStackHolder holder, ArmorMaterial material)
	{
		this.holder = holder;
		this.material = material;
	}

	@Override
	public long extractOxygen(@Nullable LivingEntity living, long amount, boolean simulate)
	{
		ItemStack stack = this.getHolder().getStack();
		float air = BacktankUtil.getAir(stack);
		float neededAir = this.toAir(amount);
		float usingAir = Math.min(air, neededAir);

		if (!simulate)
		{
			BacktankUtil.consumeAir(living, stack, usingAir);
			this.getHolder().setStack(stack);
		}

		return this.toAmount(usingAir);
	}

	@Override
	public long getOxygenAmount()
	{
		ItemStack stack = this.getHolder().getStack();
		float air = BacktankUtil.getAir(stack);
		return this.toAmount(air);
	}

	@Override
	public long getOxygenCapacity()
	{
		ItemStack stack = this.getHolder().getStack();
		float air = BacktankUtil.maxAir(stack);
		return this.toAmount(air);
	}

	private float toAir(long amount)
	{
		return (FluidHooks.toMillibuckets(amount) * ProofAbstractUtils.OXYGEN_PROOF_INTERVAL) / 20.0F;
	}

	private long toAmount(float air)
	{
		return (long) ((air * 20.0F * FluidHooks2.MILLI_BUCKET) / ProofAbstractUtils.OXYGEN_PROOF_INTERVAL);
	}

	@Override
	public Range<Integer> getTemperatureThreshold()
	{
		var suit = (this.getMaterial() == ArmorMaterials.NETHERITE) ? ModItems.NETHERITE_SPACE_SUIT.get() : ModItems.SPACE_SUIT.get();
		return ((SpaceSuit) suit).getTemperatureThreshold();
	}

	public ItemStackHolder getHolder()
	{
		return this.holder;
	}

	public ArmorMaterial getMaterial()
	{
		return this.material;
	}

}
