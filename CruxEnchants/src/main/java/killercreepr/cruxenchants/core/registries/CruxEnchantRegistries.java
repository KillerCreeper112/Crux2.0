package killercreepr.cruxenchants.core.registries;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxenchants.api.enchant.ApplicableItemType;
import killercreepr.cruxenchants.api.enchant.CruxEnchant;

public class CruxEnchantRegistries {
    public static final KeyedRegistry<CruxEnchant> CRUX_ENCHANT = KeyedRegistry.keyedRegistry();
    public static final KeyedRegistry<ApplicableItemType> APPLICABLE_ITEM_TYPE = KeyedRegistry.keyedRegistry();
}
