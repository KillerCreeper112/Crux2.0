package killercreepr.cruxenchants.core.values;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValuesProvider {
    @NotNull Holder<List<String>> enchantsTagFormat();
    default void reload(@NotNull CruxPlugin plugin){}
}
