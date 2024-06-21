package killercreepr.cruxmenu.menu.bukkit;

import killercreepr.crux.context.SimpleInputContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.cruxmenu.menu.bukkit.api.events.menu.MenuRefreshEvent;
import killercreepr.cruxmenu.menu.bukkit.holder.MenuHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigMenu extends Menu{
    protected final @NotNull MenuHolder holder;
    protected final @NotNull DataExchange info;
    protected final @NotNull MergedTagContainer tags;

    protected final Map<Integer, MenuItem> items = new HashMap<>();
    public ConfigMenu(@NotNull MenuHolder holder, @NotNull DataExchange info){
        this(holder, info, null);
    }

    public ConfigMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable MergedTagContainer tags){
        this.holder = holder;
        this.info = info;
        this.tags = new MultiTagContainer(holder.getRegistry().getFormat().tags());
        if(tags != null) this.tags.addAll(tags);
        reconstruct(Bukkit.createInventory(null, getInventorySize(buildSize()), buildTitle()));
    }

    public @NotNull Component buildTitle(){
        if(holder.getTitle() == null) return Component.empty();
        return holder.getRegistry().getFormat().deserialize(holder.getTitle(), MergedTagContainer.mergeHook(
            buildTags(), info
        ));
    }

    public int buildSize(){
        return holder.getSize().value(new SimpleInputContext(holder.getRegistry().getFormat(), buildTags())).intValue();
    }

    public @NotNull MergedTagContainer buildTags(){
        MergedTagContainer tags = new MultiTagContainer(holder.getRegistry().getFormat().tags());
        tags.hookAll(info());
        tags.addAll(getTags());
        return tags;
    }
    public @NotNull DataExchange info(){
        return info;
    }

    /**
     * Sets the MenuHolder's items and click actions.
     */
    public ConfigMenu setItems(@NotNull MenuHolder holder){
        Player viewer = info.getOrThrow("viewer", Player.class);
        MenuContext menuContext = new MenuContext(this, info, tags);
        holder.getItems().items().forEach(menuItem -> {
            MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
            Optional<Integer> slot = i.getSlot();
            if(slot.isEmpty() || !i.canDisplay()) return;
            setItem(slot.get(), i, viewer);
        });
        return this;
    }

    /**
     * Resets the inventory. Namely, clears the items and click actions.
     */
    public ConfigMenu reset(){
        items.clear();
        inventory.clear();
        clearActions();
        return this;
    }

    /**
     * Called after creation.
     */
    public ConfigMenu load(){
        refresh();
        return this;
    }

    public MenuRefreshEvent refresh(){
        MenuRefreshEvent event = new MenuRefreshEvent(this);
        if(!event.callEvent()) return event;
        clearActions();
        setItems(holder);
        return event;
    }

    public ConfigMenu setItem(int slot, @Nullable MenuItem item, @NotNull Player viewer){
        return setItem(slot, item, item==null?null:item.buildItem(viewer));
    }

    public ConfigMenu setItem(int slot, @Nullable MenuItem item, @Nullable ItemStack display){
        if(item == null){
            items.remove(slot);
            setItem(slot, null, (MenuClick) null);
            return this;
        }
        items.put(slot, item);
        setItem(slot, display, item::click);
        return this;
    }

    public @NotNull MenuHolder getHolder() {
        return holder;
    }

    public @NotNull MergedTagContainer getTags() {
        return tags;
    }
}
