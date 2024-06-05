package killercreepr.cruxenchants.values;

import killercreepr.crux.data.Holder;
import killercreepr.crux.plugin.CruxPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValuesProvider {
    @NotNull Holder<List<String>> enchantsTagFormat();
    default void reload(@NotNull CruxPlugin plugin){}
}
