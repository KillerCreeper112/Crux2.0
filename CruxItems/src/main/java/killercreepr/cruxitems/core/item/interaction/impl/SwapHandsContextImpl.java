package killercreepr.cruxitems.core.item.interaction.impl;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxitems.api.item.interaction.SwapHandsContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SwapHandsContextImpl implements SwapHandsContext {
    public static Builder builder(){
        return new Builder();
    }

    protected final @NotNull Player player;
    protected final @NotNull CruxItem item;
    protected final ItemStack mainHandItem;
    protected final ItemStack offHandItem;

    public SwapHandsContextImpl(@NotNull Player player, @NotNull CruxItem item, ItemStack mainHandItem, ItemStack offHandItem) {
        this.player = player;
        this.item = item;
        this.mainHandItem = mainHandItem;
        this.offHandItem = offHandItem;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public ItemStack getMainHandItem() {
        return mainHandItem;
    }

    @Override
    public ItemStack getOffhandItem() {
        return offHandItem;
    }

    @Override
    public @NotNull CruxItem getItem() {
        return item;
    }


    public static final class Builder {
        private @NotNull Player player;
        private ItemStack mainHandItem;
        private ItemStack offHandItem;
        private CruxItem item;

        public Builder player(Player player) {
            this.player = player;
            return this;
        }
        public Builder item(CruxItem item) {
            this.item = item;
            return this;
        }
        public Builder mainHandItem(ItemStack mainHandItem) {
            this.mainHandItem = mainHandItem;
            return this;
        }
        public Builder offHandItem(ItemStack offHandItem) {
            this.offHandItem = offHandItem;
            return this;
        }

        public SwapHandsContextImpl build() {
            return new SwapHandsContextImpl(player, item, mainHandItem, offHandItem);
        }
    }
}
