package ad_astra_giselle_addon.common.content.oxygen;

import earth.terrarium.botarium.common.item.ItemStackHolder;

public interface IOxygenChargerItem extends IOxygenStorageItem
{
	IOxygenCharger getOxygenCharger(ItemStackHolder item);

	@Override
	default IOxygenStorage getOxygenStorage(ItemStackHolder item)
	{
		return this.getOxygenCharger(item);
	}

}
