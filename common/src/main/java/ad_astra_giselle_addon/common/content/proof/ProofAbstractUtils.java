package ad_astra_giselle_addon.common.content.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ad_astra_giselle_addon.common.AdAstraGiselleAddon;
import ad_astra_giselle_addon.common.entity.EntityCustomDataHelper;
import ad_astra_giselle_addon.common.fluid.FluidHooks2;
import ad_astra_giselle_addon.common.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public abstract class ProofAbstractUtils
{
	private static final List<ProofAbstractUtils> _PROOFS = new ArrayList<>();
	public static final List<ProofAbstractUtils> PROOFS = Collections.unmodifiableList(_PROOFS);

	public static final String KEY_PROOF_MAP = AdAstraGiselleAddon.rl("proof").toString();
	public static final String KEY_PROOF_DURATION = "proof_duration";

	public static final int GENERAL_PROOF_INTERVAL = 10;
	public static final int OXYGEN_PROOF_INTERVAL = 30;
	public static final long OXYGEN_PROOF_USING = FluidHooks2.MILLI_BUCKET;

	public static CompoundTag getAllCustomData(Entity entity)
	{
		return NBTUtils.getOrCreateTag(EntityCustomDataHelper.getCustomData(entity), KEY_PROOF_MAP);
	}

	public static void reduceProofDuration(Entity entity)
	{
		for (ProofAbstractUtils proof : PROOFS)
		{
			int currentDuration = proof.getProofDuration(entity);

			if (currentDuration > 0)
			{
				proof.setProofDuration(entity, currentDuration - 1);
			}

		}

	}

	private final ResourceLocation id;
	private final String customDataKey;
	private final List<ProofFunction> listeners;

	protected ProofAbstractUtils(ResourceLocation id)
	{
		this.id = id;
		this.customDataKey = id.toString();
		this.listeners = new ArrayList<>();
		_PROOFS.add(this);
	}

	public ResourceLocation getId()
	{
		return this.id;
	}

	public String getCustomDataKey()
	{
		return this.customDataKey;
	}

	public CompoundTag getCustomData(Entity entity)
	{
		return NBTUtils.getTag(getAllCustomData(entity), this.getCustomDataKey());
	}

	public CompoundTag getOrCreateData(Entity entity)
	{
		return NBTUtils.getOrCreateTag(getAllCustomData(entity), this.getCustomDataKey());
	}

	public int getProofDuration(Entity entity)
	{
		return this.getCustomData(entity).getInt(KEY_PROOF_DURATION);
	}

	public void setProofDuration(Entity entity, int proofDuration)
	{
		this.getOrCreateData(entity).putInt(KEY_PROOF_DURATION, proofDuration);
	}

	public boolean tryProvideProof(Entity entity)
	{
		if (this.getProofDuration(entity) > 0)
		{
			return true;
		}
		else
		{
			int proofDuration = this.post(entity);

			if (proofDuration > 0)
			{
				this.setProofDuration(entity, proofDuration);
				return true;
			}

			return false;
		}

	}

	public void register(ProofFunction event)
	{
		this.listeners.add(event);
	}

	public boolean unregister(ProofFunction event)
	{
		return this.listeners.remove(event);
	}

	public int post(Entity entity)
	{
		for (ProofFunction event : this.listeners)
		{
			int proofFunction = event.provide(entity);

			if (proofFunction > 0)
			{
				return proofFunction;
			}

		}

		return 0;
	}

}
