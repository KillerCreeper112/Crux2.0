package killercreepr.cruxitems.item;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface CruxedItemUpdater extends Keyed {
    void onUpdate(@NotNull CruxedItemUpdateContext context);
}
