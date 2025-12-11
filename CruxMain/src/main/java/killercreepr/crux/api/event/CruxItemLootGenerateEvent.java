package killercreepr.crux.api.event;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CruxItemLootGenerateEvent extends Event implements Cancellable {
    private final static HandlerList handlers = new HandlerList();

    protected final LootTable<ItemStack> lootTable;
    protected final LootContext lootContext;
    protected final List<ItemStack> loot;

    public CruxItemLootGenerateEvent(LootTable<ItemStack> lootTable, LootContext lootContext, List<ItemStack> loot) {
        this.lootTable = lootTable;
        this.lootContext = lootContext;
        this.loot = loot;
    }

    public LootTable<ItemStack> getLootTable() {
        return lootTable;
    }

    public LootContext getLootContext() {
        return lootContext;
    }

    public List<ItemStack> getLoot() {
        return loot;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList(){ return handlers; }

    private boolean cancel = false;
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
