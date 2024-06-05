package killercreepr.cruxitems.values;

import killercreepr.crux.data.Holder;
import killercreepr.crux.data.Reloadable;
import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValuesProvider extends Reloadable {
    @NotNull Holder<List<String>> generalItemLoreFormat();
    @NotNull Holder<String> generalItemNameFormat();
    @Override
    default void reload(@NotNull CruxPlugin plugin){}
}
