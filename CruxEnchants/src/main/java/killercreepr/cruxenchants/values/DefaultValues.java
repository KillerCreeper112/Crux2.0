package killercreepr.cruxenchants.values;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultValues implements ValuesProvider{
    public static final Holder<List<String>> ENCHANTS_TAG_FORMAT = Holder.direct(List.of(
        "<gray><enchant_name> <enchant_level>"
    ));
    @Override
    public @NotNull Holder<List<String>> enchantsTagFormat() {
        return ENCHANTS_TAG_FORMAT;
    }
}
