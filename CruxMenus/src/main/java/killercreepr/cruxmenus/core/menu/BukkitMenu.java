package killercreepr.cruxmenus.core.menu;

import killercreepr.cruxmenus.api.event.MenuCloseEvent;
import killercreepr.cruxmenus.api.event.MenuOpenEvent;
import killercreepr.cruxmenus.api.event.MenuRefreshEvent;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.module.MenuModuleRegistry;
import killercreepr.cruxmenus.api.menu.slot.Slot;
import killercreepr.cruxmenus.core.menu.module.MenuModuleRegistryImpl;
import killercreepr.cruxmenus.core.registries.Menus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class BukkitMenu implements Menu {
    protected final LinkedHashMap<Integer, Slot> slots = new LinkedHashMap<>();
    protected final @NotNull UUID uuid;
    protected @NotNull Inventory inventory;

    protected final MenuModuleRegistry modules = new MenuModuleRegistryImpl(this);
    protected boolean refreshing = false;

    public BukkitMenu(){
        this(UUID.randomUUID());
    }

    public BukkitMenu(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public BukkitMenu(@NotNull UUID uuid, @NotNull Inventory inventory) {
        this.uuid = uuid;
        this.inventory = inventory;
    }

    protected void addSlot(@NotNull Slot slot){
        addSlot(slot, true);
    }

    protected void addSlot(@NotNull Slot slot, boolean skipUpdate){
        slots.put(slot.getIndex(), slot);
        if(skipUpdate) return;
        slot.setItem(slot.getSlottedItemReplacement(), true);
    }

    public void openSilently(@NotNull HumanEntity p){
        p.openInventory(inventory);
        Menus.OPENED.register(p.getUniqueId(), this);
    }

    public Menu reconstruct(int size, @NotNull Component name, boolean keepOldContents, boolean reopenSilently){
        Inventory old = inventory;
        reconstruct(size, name);
        if(old != null){
            if(keepOldContents){
                inventory.setContents(old.getContents());
            }
            if(reopenSilently){
                for(HumanEntity e : new ArrayList<>(old.getViewers())){
                    openSilently(e);
                }
            }
        }
        return this;
    }

    @Override
    public Menu reconstruct(int size, @NotNull Component name) {
        return reconstruct(Bukkit.createInventory(this, size, name));
    }

    @Override
    public Menu reconstruct(@NotNull Inventory inv) {
        inventory = inv;
        return this;
    }

    @Override
    public @NotNull MenuOpenEvent open(@NotNull Player p) {
        MenuOpenEvent event = new MenuOpenEvent(p, this);
        if(!event.callEvent()) return event;
        onOpen(p);
        return event;
    }

    @Override
    public void onOpen(@NotNull Player p) {
        p.openInventory(inventory);
        Menus.OPENED.register(p.getUniqueId(), this);

        slots.values().forEach(slot -> slot.onMenuOpen(p));
        modules.forEach(m -> m.onOpen(p, this));
    }

    @Override
    public @NotNull MenuCloseEvent close(@NotNull Player p) {
        Menu menu = Menus.getOpened(p);
        if(menu == null || !menu.uuid().equals(uuid))
            throw new UnsupportedOperationException("Menu, " + this + " has not been opened by " + p.getName());

        MenuCloseEvent event = new MenuCloseEvent(p, this);
        if(!event.callEvent()) return event;
        onClose(p);
        return event;
    }

    @Override
    public void onClose(@NotNull Player p) {
        Menus.OPENED.remove(p.getUniqueId());

        slots.values().forEach(slot -> slot.onMenuClose(p));
        modules.forEach(m -> m.onClose(p, this));
    }

    @Override
    public MenuRefreshEvent refresh() {
        MenuRefreshEvent event = new MenuRefreshEvent(this);
        if(!event.callEvent()) return event;

        onRefresh();
        return event;
    }

    @Override
    public void onRefresh(){
        setRefreshing(true);
        modules.refresh();
        setRefreshing(false);
    }

    @Override
    public boolean isRefreshing() {
        return refreshing;
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
    }

    @Override
    public @NotNull UUID uuid() {
        return uuid;
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item, boolean silent) {
        inventory.setItem(index, item);
        if(!silent) onUpdate();
    }

    @Override
    public void putSlot(int index, @Nullable Slot slot) {
        if(slot == null){
            slots.remove(index);
            return;
        }
        slots.put(index, slot);
    }

    @Override
    public @Nullable Slot getSlot(int index) {
        return slots.get(index);
    }

    @Override
    public @NotNull Map<Integer, Slot> getSlots() {
        return slots;
    }

    @Override
    public void onUpdate() {
        updateSlots();
        modules.forEach(m -> m.onUpdate(this));
    }

    public void updateSlots(){
        slots.values().forEach(Slot::onMenuUpdate);
    }

    @Override
    public int buildSize() {
        return 0;//todo abstract
    }

    @Override
    public @NotNull Component buildTitle() {
        return null;//todo abstract
    }

    @Override
    public @NotNull MenuModuleRegistry getModules() {
        return modules;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
