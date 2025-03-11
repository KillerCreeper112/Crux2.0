package killercreepr.cruxitems.api.item.context;

import killercreepr.crux.api.item.CruxItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ItemedContext {
    @NotNull
    CruxItem getItem();
}
