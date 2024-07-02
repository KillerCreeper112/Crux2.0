package killercreepr.cruxmenus.menu.bukkit;

import killercreepr.crux.context.SimpleInputContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.crux.util.InvUtil;
import killercreepr.cruxmenus.menu.bukkit.api.events.menu.MenuRefreshEvent;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigMenu extends BukkitMenu implements CfgMenu {
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
        reconstruct(Bukkit.createInventory(this, InvUtil.getInventorySize(buildSize()), buildTitle()));
    }

    @Override
    public @NotNull Component buildTitle(){
        if(holder.getTitle() == null) return Component.empty();
        return holder.getRegistry().getFormat().deserialize(holder.getTitle(), MergedTagContainer.mergeHook(
            buildTags(), info
        ));
    }

    @Override
    public int buildSize(){
        return holder.getSize().sample(new SimpleInputContext(holder.getRegistry().getFormat(), buildTags())).intValue();
    }

    @Override
    public @Nullable MergedTagContainer buildTags(){
        MergedTagContainer tags = new MultiTagContainer(holder.getRegistry().getFormat().tags());
        tags.hookAll(info());
        tags.addAll(getTags());
        return tags;
    }

    @Override
    public @NotNull DataExchange info(){
        return info;
    }

    /**
     * Sets the MenuHolder's items and click actions.
     */
    @Override
    public void setItems(@NotNull MenuHolder holder){
        Player viewer = info.getOrThrow("viewer", Player.class);
        MenuContext menuContext = new MenuContext(this, info, tags);
        holder.getItems().items().forEach(menuItem -> {
            MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
            Optional<Integer> slot = i.getSlot();
            if(slot.isEmpty() || !i.canDisplay()) return;
            setItem(slot.get(), i, viewer);
        });
    }

    @Override
    public @Nullable MenuItem setItem(@NotNull MenuHolder holder, int index){
        Player viewer = info.getOrThrow("viewer", Player.class);
        MenuContext menuContext = new MenuContext(this, info, tags);
        return setItem(holder, index, viewer, menuContext);
    }

    public @Nullable MenuItem setItem(@NotNull MenuHolder holder, int index, @NotNull Player viewer, @NotNull MenuContext menuContext){
        Collection<MenuItemHolder> potentialItems = holder.getItems().getItems().get(index);
        if(potentialItems == null || potentialItems.isEmpty()) return null;

        MenuItem last = null;
        for(MenuItemHolder menuItem : potentialItems){
            MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
            Optional<Integer> slot = i.getSlot();
            if(slot.isEmpty() || !i.canDisplay()) continue;
            setItem(slot.get(), i, viewer);
            last = i;
        }
        return last;
    }

    @Override
    public void reset(){
        super.reset();
        items.clear();
    }

    @Override
    public MenuRefreshEvent refresh() {
        MenuRefreshEvent event = super.refresh();
        if(event.isCancelled()) return event;
        setItems(holder);
        return event;
    }

    @Override
    public void setItem(int slot, @Nullable MenuItem item, @Nullable ItemStack display, boolean silent){
        if(item == null){
            items.remove(slot);
            setItem(slot, null, silent);
            return;
        }
        items.put(slot, item);
        setItem(slot, display, silent);
    }

    @Override
    public @NotNull MenuHolder getHolder() {
        return holder;
    }

    @Override
    public @NotNull Map<Integer, MenuItem> getMenuItems() {
        return items;
    }

    @Override
    public void clearMenuItems(boolean silent) {
        items.clear();
        if(!silent) onUpdate();
    }

    public @NotNull MergedTagContainer getTags() {
        return tags;
    }

    @Override
    public void onMenuClick(@NotNull InventoryClickEvent event) {
        super.onMenuClick(event);

        MenuItem menuItem = items.get(event.getSlot());
        if(menuItem==null) return;
        if(!(event.getWhoClicked() instanceof Player p)) return;
        menuItem.click(p, event);
    }
}
