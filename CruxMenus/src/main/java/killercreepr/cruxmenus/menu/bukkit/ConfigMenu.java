package killercreepr.cruxmenus.menu.bukkit;

import killercreepr.crux.context.SimpleInputContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.crux.util.InvUtil;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItems;
import net.kyori.adventure.text.Component;
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
    public @NotNull MergedTagContainer buildTags(@NotNull TagParser tagParser){
        MergedTagContainer tags = new MultiTagContainer(tagParser);
        tags.addAll(super.buildTags(tagParser));
        tags.hookAll(info());
        tags.addAll(getTags());
        return tags;
    }

    @Override
    public @NotNull DataExchange info(){
        return info;
    }

    @Override
    public @NotNull MergedTagContainer buildTags() {
        return buildTags(holder.getRegistry().getFormat().tags());
    }

    /**
     * Sets the MenuHolder's items and click actions.
     */
    @Override
    public void setItems(@NotNull MenuHolder holder){
        setItems(holder.getItems());
    }

    @Override
    public void setItems(@NotNull MenuHolder holder, @NotNull MenuContext ctx) {
        setItems(holder.getItems(), ctx);
    }

    @Override
    public void setItems(@NotNull MenuItems items){
        MenuContext menuContext = new MenuContext(this, info, tags);
        setItems(items, menuContext);
    }

    @Override
    public void setItems(@NotNull MenuItems items, @NotNull MenuContext menuContext) {
        Player viewer = info.getOrThrow("viewer", Player.class);
        items.forEach(list -> list.forEach(menuItem ->{
            MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
            Optional<Integer> slot = i.getSlot();

            if(slot.isEmpty() || !i.canDisplay()) return;
            setItem(slot.get(), i, viewer);
        }));
    }

    public @Nullable MenuItem setItem(@NotNull MenuItemHolder menuItem, @NotNull Player viewer, @NotNull MenuContext menuContext){
        MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
        Optional<Integer> slot = i.getSlot();
        if(slot.isEmpty() || !i.canDisplay()) return i;
        setItem(slot.get(), i, viewer);
        return i;
    }

    @Override
    public @Nullable MenuItem setItem(@NotNull MenuItemHolder menuItem, @NotNull MenuContext menuContext) {
        return setItem(menuItem, info.getOrThrow("viewer", Player.class), menuContext);
    }

    public void setItem(int index, @NotNull MenuItemHolder menuItem, @NotNull Player viewer, @NotNull MenuContext menuContext){
        MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
        if(!i.canDisplay()) return;
        setItem(index, i, viewer);
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

    protected void refreshReconstruct(){
        reconstruct(InvUtil.getInventorySize(buildSize()), buildTitle(), true, true);
    }

    @Override
    public void onRefresh() {
        refreshReconstruct();

        clearItems(true);
        clearMenuItems(true);
        modules.refresh();
        setItems(holder);
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
