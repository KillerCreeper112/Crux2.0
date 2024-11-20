package killercreepr.cruxitems.values;

import killercreepr.crux.api.data.Holder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//todo "{cruxenchants_enchants}",
public class DefaultValues implements ValuesProvider{
    public static final Holder<List<String>> GENERAL_ITEM_LORE_FORMAT = Holder.direct(List.of(
        "{cruxpotions_potions}"
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
