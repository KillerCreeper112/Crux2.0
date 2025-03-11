package killercreepr.cruxitems.core.item.consume;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.item.consume.ItemConsumeContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemConsumeContextImpl implements ItemConsumeContext {
    protected final Player player;
    protected final CruxItem item;
    protected final ItemStack replacement;
    protected final EquipmentSlot slot;

    public ItemConsumeContextImpl(Player player, CruxItem item, ItemStack replacement, EquipmentSlot slot) {
        this.player = player;
        this.item = item;
        this.replacement = replacement;
        this.slot = slot;
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

    @Override
    public @NotNull EquipmentSlot getSlot() {
        return slot;
    }
}
