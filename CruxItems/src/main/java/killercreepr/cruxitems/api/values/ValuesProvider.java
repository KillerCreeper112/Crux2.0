package killercreepr.cruxitems.api.values;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.api.item.CruxItemType;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxitems.core.item.CfgItemType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface ValuesProvider extends Reloadable {
    @NotNull Holder<List<String>> generalItemLoreFormat();
    @NotNull Holder<String> generalItemNameFormat();
    @NotNull Holder<Map<Key, CfgItemType>> itemTypes();
    @Override
    default void reload(@NotNull CruxPlugin plugin){}
}
