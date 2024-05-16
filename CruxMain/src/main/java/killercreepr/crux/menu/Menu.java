package killercreepr.crux.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Menu {
    private static final Map<UUID, Menu> OPEN_MENUS = new HashMap<>();
    public static @NotNull Optional<Menu> getMenu(@NotNull Player p){ return Optional.ofNullable(OPEN_MENUS.getOrDefault(p.getUniqueId(), null)); }
    public static int getInventorySize(int length){
        int quotient = (int) Math.ceil(length / 9f);
        return quotient > 5 ? 54 : quotient * 9;
    }

    protected final Map<Integer, MenuClick> clickActions = new HashMap<>();

    protected @Nullable MenuClick generalClickAction;
    protected @Nullable MenuClick generalInvClickAction;
    protected @Nullable MenuDrag generalDragAction;

    protected @Nullable MenuOpen openAction;
    protected @Nullable MenuClose closeAction;
    protected final @NotNull UUID uuid = UUID.randomUUID();
    protected @NotNull Inventory inventory;

    public Menu(@NotNull Inventory inventory){
        this.inventory = inventory;
    }

    /**
     * WARNING! This will cause errors if you do not set an inventory!
     */
    public Menu(){}

    protected Menu reconstruct(int size, @NotNull Component name){
        inventory = Bukkit.createInventory(null, size, name);
        return this;
    }

    protected Menu reconstruct(@NotNull Inventory inv){
        inventory = inv;
        return this;
    }

    public @NotNull Menu open(@NotNull Player p){
        p.openInventory(inventory);
        OPEN_MENUS.put(p.getUniqueId(), this);
        if(openAction != null) openAction.open(p);
        return this;
    }

    public boolean close(@NotNull Player p){
        return OPEN_MENUS.entrySet().removeIf(entry ->{
            if(entry.getValue().getUuid().equals(uuid) && entry.getKey().equals(p.getUniqueId())){
                if(closeAction != null) closeAction.close(p);
                return true;
            }
            return false;
        });
    }

    public boolean close(){
        return OPEN_MENUS.entrySet().removeIf(entry ->{
            if(entry.getValue().getUuid().equals(uuid)){
                Player p = Bukkit.getPlayer(entry.getKey());
                if(p != null){
                    if(closeAction != null) closeAction.close(p);
                }
                return true;
            }
            return false;
        });
    }

    public Map<Integer, MenuClick> getClickActions() {
        return clickActions;
    }

    public MenuClick getGeneralClickAction() {
        return generalClickAction;
    }

    public MenuClick getGeneralInvClickAction() {
        return generalInvClickAction;
    }

    public MenuDrag getGeneralDragAction() {
        return generalDragAction;
    }

    public MenuOpen getOpenAction() {
        return openAction;
    }

    public MenuClose getCloseAction() {
        return closeAction;
    }

    public UUID getUuid() {
        return uuid;
    }

    public MenuClick getAction(int index){ return clickActions.getOrDefault(index, null); }

    protected Menu setGeneralClickAction(@Nullable MenuClick generalClickAction) { this.generalClickAction = generalClickAction; return this; }

    protected Menu setGeneralInvClickAction(@Nullable MenuClick generalInvClickAction) { this.generalInvClickAction = generalInvClickAction; return this; }

    protected Menu setGeneralDragAction(@Nullable MenuDrag generalDragAction) { this.generalDragAction = generalDragAction; return this; }
    protected Menu setOpenAction(@Nullable MenuOpen openAction) { this.openAction = openAction; return this; }
    protected Menu setCloseAction(@Nullable MenuClose closeAction) { this.closeAction = closeAction; return this; }

    public interface MenuClick{ void click(@NotNull Player p, @NotNull InventoryClickEvent event); }
    public interface MenuDrag{ void drag(@NotNull Player p, @NotNull InventoryDragEvent event); }
    public interface MenuOpen{void open(@NotNull Player p); }
    public interface MenuClose{void close(@NotNull Player p); }


    public Menu setItem(int index, @Nullable ItemStack item){ inventory.setItem(index, item); return this; }
    public Menu setItem(int index, @Nullable ItemStack item, @Nullable MenuClick action){
        inventory.setItem(index, item);
        setAction(index, action);
        return this;
    }

    public Menu setAction(int index, @Nullable MenuClick action){
        if(action == null) clickActions.remove(index);
        else clickActions.put(index, action);
        return this;
    }

    public Menu clearActions(){
        clickActions.clear();
        return this;
    }

    public @NotNull Inventory getInventory(){ return inventory; }

    protected @NotNull MenuDrag defaultMenuDrag(){
        return (p, event) -> {
            if(event.getRawSlots().size() == 1){
                for(int i : event.getRawSlots()){
                    if(clickActions.containsKey(i)){
                        MenuClick c = clickActions.getOrDefault(i, null);
                        if(c == null) continue;
                        c.click(p,
                                new InventoryClickEvent(event.getView(), event.getView().getSlotType(i),
                                        i, ClickType.LEFT, InventoryAction.PLACE_ALL));
                        return;
                    }
                }
            }
            for(int s : event.getRawSlots()){
                if(s < event.getView().getTopInventory().getSize()) return;
            }
            event.setCancelled(false);
        };
    }
}
