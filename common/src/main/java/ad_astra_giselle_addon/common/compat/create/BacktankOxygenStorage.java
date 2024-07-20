package ad_astra_giselle_addon.common.compat.create;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.equipment.armor.BacktankUtil;

import ad_astra_giselle_addon.common.content.oxygen.IOxygenStorage;
import ad_astra_giselle_addon.common.content.proof.ProofAbstractUtils;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.item.ItemStackHolder;
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
		return (FluidConstants.toMillibuckets(amount) * ProofAbstractUtils.OXYGEN_PROOF_INTERVAL) / 20.0F;
	}

	private long toAmount(float air)
	{
		return (long) ((air * 20.0F * FluidConstants.fromMillibuckets(1)) / ProofAbstractUtils.OXYGEN_PROOF_INTERVAL);
	}

	@Override
	public boolean canUseOnCold()
	{
		return true;
	}

	@Override
	public boolean canUseOnHot()
	{
		return this.getMaterial() == ArmorMaterials.NETHERITE;
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
