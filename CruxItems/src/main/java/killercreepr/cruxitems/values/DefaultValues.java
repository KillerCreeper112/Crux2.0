package killercreepr.cruxitems.values;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultValues implements ValuesProvider{
    public static final Holder<List<String>> GENERAL_ITEM_LORE_FORMAT = Holder.direct(List.of(
        "<gray><enchant_name> <enchant_level>"
    ));

    public static final Holder<String> GENERAL_ITEM_NAME_FORMAT = Holder.empty();
    @Override
    public @NotNull Holder<List<String>> generalItemLoreFormat() {
        return GENERAL_ITEM_LORE_FORMAT;
    }

    @Override
    public @NotNull Holder<String> generalItemNameFormat() {
        return GENERAL_ITEM_NAME_FORMAT;
    }
}
