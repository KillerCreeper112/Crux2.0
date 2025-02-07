package killercreepr.cruxitems.core.item.consume;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.item.consume.ItemConsumeContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemConsumeContextImpl implements ItemConsumeContext {
    protected final Player player;
    protected final CruxItem item;
    protected final ItemStack replacement;

    public ItemConsumeContextImpl(Player player, CruxItem item, ItemStack replacement) {
        this.player = player;
        this.item = item;
        this.replacement = replacement;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull CruxItem getItem() {
        return item;
    }

    @Override
    public @Nullable ItemStack getReplacement() {
        return replacement;
    }
}
