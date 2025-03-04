package killercreepr.cruxattributes.api.values;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.core.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValuesProvider extends Reloadable {
    @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_FORMAT();
    @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_FORMAT();
    @NotNull Holder<List<String>> CRUX_ATTRIBUTES_ITEM_MODIFIER_MULTIPLY_FORMAT();
    @Override
    default void reload(@NotNull CruxPlugin plugin){}
}
