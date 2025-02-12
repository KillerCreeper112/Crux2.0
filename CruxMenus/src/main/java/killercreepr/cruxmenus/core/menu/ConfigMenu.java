package killercreepr.cruxmenus.core.menu;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.container.SimpleMergedTagContainer;
import killercreepr.crux.core.text.context.SimpleInputContext;
import killercreepr.crux.core.util.InvUtil;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConfigMenu extends BukkitMenu implements CfgMenu {
    protected final @NotNull MenuHolder holder;
    protected @NotNull DataExchange info;
    protected final @NotNull MergedTagContainer tags;

    protected final Map<Integer, MenuItem> items = new HashMap<>();
    public ConfigMenu(@NotNull MenuHolder holder, @NotNull DataExchange info){
        this(holder, info, null);
    }

    public ConfigMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable MergedTagContainer tags){
        this.holder = holder;
        this.info = info;
        this.tags = TagContainer.merged(holder.getRegistry().getFormat().tags());
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
        MergedTagContainer tags = new SimpleMergedTagContainer(tagParser);
        tags.addAll(super.buildTags(tagParser));
        tags.hookAllWithPrefix(info());
        if(info.get("viewer") instanceof OfflinePlayer viewer){
            tags.addAll(tags.hook(viewer, TagsPrefixBuilder.overwriteCompletely("viewer_")));
        }
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
        MenuContext menuContext = MenuContext.context(this, info, tags);
        setItems(items, menuContext);
    }

    //
    public Map<Integer, MenuItem> buildItems(@NotNull MenuItems items, @NotNull MenuContext menuContext){
        Player viewer = info.getOrThrow("viewer", Player.class);
        Map<Integer, MenuItem> map = new HashMap<>();
        items.forEach(list -> list.forEach(menuItem ->{
            MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
            Optional<List<Number>> slot = i.getSlots();

            if(slot.isEmpty() || !i.canDisplay()) return;
            slot.get().forEach(num -> map.put(num.intValue(), i));
            //map.put(slot.get(), i);
            //setItem(slot.get(), i, viewer);
        }));
        return map;
    }

    @Override
    public void setItems(@NotNull MenuItems items, @NotNull MenuContext menuContext) {
        Player viewer = info.getOrThrow("viewer", Player.class);
        buildItems(items, menuContext).forEach((slot, item) ->{
            setItem(slot, item, viewer);
        });
        /*items.forEach(list -> list.forEach(menuItem ->{
            MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
            Optional<Integer> slot = i.getSlot();

            if(slot.isEmpty() || !i.canDisplay()) return;
            setItem(slot.get(), i, viewer);
        }));*/
    }

    @Override
    public CfgMenu info(@NotNull DataExchange info) {
        this.info = info;
        return this;
    }

    public @Nullable MenuItem setItem(@NotNull MenuItemHolder menuItem, @NotNull Player viewer, @NotNull MenuContext menuContext){
        MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
        Optional<List<Number>> slot = i.getSlots();
        if(slot.isEmpty() || !i.canDisplay()) return i;
        slot.get().forEach(num ->{
            setItem(num.intValue(), i, viewer);
        });
        //setItem(slot.get(), i, viewer);
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
        MenuContext menuContext = MenuContext.context(this, info, tags);
        return setItem(holder, index, viewer, menuContext);
    }

    public @Nullable MenuItem setItem(@NotNull MenuHolder holder, int index, @NotNull Player viewer, @NotNull MenuContext menuContext){
        Collection<MenuItemHolder> potentialItems = holder.getItems().get(index);
        if(potentialItems == null || potentialItems.isEmpty()) return null;

        MenuItem last = null;
        for(MenuItemHolder menuItem : potentialItems){
            MenuItem i = menuItem.getDisplayItem(viewer, menuContext);
            Optional<List<Number>> slot = i.getSlots();
            if(slot.isEmpty() || !i.canDisplay()) continue;
            slot.get().forEach(num ->{
                setItem(num.intValue(), i, viewer);
            });
            //setItem(slot.get(), i, viewer);
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
        setRefreshing(true);
        refreshReconstruct();

        clearItems(true);
        clearMenuItems(true);
        modules.refresh();
        setItems(holder);
        setRefreshing(false);
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
