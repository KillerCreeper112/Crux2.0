package killercreepr.cruxitems.api.item;

import killercreepr.cruxitems.core.item.CruxedItemUpdateContext;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface CruxedItemUpdater extends Keyed {
    void onUpdate(@NotNull CruxedItemUpdateContext context);
}
