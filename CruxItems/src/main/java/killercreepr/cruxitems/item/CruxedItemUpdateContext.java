package killercreepr.cruxitems.item;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxedItemUpdateContext {
    protected final @NotNull CruxedItem item;
    protected final @Nullable Entity holder;

    public CruxedItemUpdateContext(@NotNull CruxedItem item, @Nullable Entity holder) {
        this.item = item;
        this.holder = holder;
    }

    public @NotNull CruxedItem getItem() {
        return item;
    }

    public @Nullable Entity getHolder() {
        return holder;
    }
}
