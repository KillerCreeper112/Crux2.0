package killercreepr.cruxitems.api.item.plugin;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import org.jetbrains.annotations.NotNull;

public interface DynamicPluginItem extends PluginItem{
    @NotNull
    DynamicItem dynamicItem();
}
