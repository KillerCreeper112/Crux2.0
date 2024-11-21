package killercreepr.cruxitems.api.values;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.core.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValuesProvider extends Reloadable {
    @NotNull Holder<List<String>> generalItemLoreFormat();
    @NotNull Holder<String> generalItemNameFormat();
    @Override
    default void reload(@NotNull CruxPlugin plugin){}
}
