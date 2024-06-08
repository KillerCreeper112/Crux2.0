package killercreepr.cruxitems.registries;

import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxitems.item.plugin.PluginItem;

public class CruxItemRegistries {
    public static final CruxedItemUpdaterRegistry ITEM_UPDATERS = new CruxedItemUpdaterRegistry();
    public static final KeyedRegistry<PluginItem> ITEMS = new SimpleKeyedRegistry<>();
}
