package killercreepr.cruxmenus.menu.bukkit;

import killercreepr.crux.Crux;
import killercreepr.crux.util.CruxItem;
import killercreepr.cruxmenus.menu.bukkit.api.events.menu.MenuCloseEvent;
import killercreepr.cruxmenus.menu.bukkit.api.events.menu.MenuOpenEvent;
import killercreepr.cruxmenus.menu.bukkit.api.events.menu.slot.MenuSlotGiveEvent;
import killercreepr.cruxmenus.menu.bukkit.api.events.menu.slot.MenuSlotTakeEvent;
import killercreepr.cruxmenus.menu.bukkit.listener.MenuListener;
import killercreepr.cruxmenus.menu.bukkit.slot.Slot;
import killercreepr.cruxmenus.menu.bukkit.slot.SlotContext;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Menu implements InventoryHolder {
    //player uuid -> menu
    private static final Map<UUID, Menu> OPEN_MENUS = new HashMap<>();
    public static @NotNull Map<UUID, Menu> getOpenedMenus(){ return OPEN_MENUS; }
    public static @NotNull Optional<Menu> getMenu(@NotNull Player p){ return Optional.ofNullable(menu(p)); }
    public static @Nullable Menu menu(@NotNull Player p){
        return OPEN_MENUS.get(p.getUniqueId());
    }
    public static int getInventorySize(int length){
        int quotient = (int) Math.ceil(length / 9f);
        return quotient > 5 ? 54 : quotient * 9;
    }

    protected final Map<Integer, MenuClick> clickActions = new HashMap<>();
    protected final LinkedHashMap<Integer, Slot> slots = new LinkedHashMap<>();

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
        inventory = Bukkit.createInventory(this, size, name);
        return this;
    }

    protected Menu reconstruct(@NotNull Inventory inv){
        inventory = inv;
        return this;
    }

    public @NotNull MenuOpenEvent open(@NotNull Player p){
        MenuOpenEvent event = new MenuOpenEvent(p, this);
        if(!event.callEvent()) return event;

        p.openInventory(inventory);
        OPEN_MENUS.put(p.getUniqueId(), this);
        if(openAction != null) openAction.open(p);
        slots.values().forEach(slot -> slot.onMenuOpen(p));
        return event;
    }

    /**
     * This does not actually close the player's inventory.
     * Use {@link Player#closeInventory()} instead and this method will automatically
     * be called. See {@link MenuListener}
     */
    public @NotNull MenuCloseEvent close(@NotNull Player p){
        Menu menu = menu(p);
        if(menu == null || !menu.getUuid().equals(uuid))
            throw new UnsupportedOperationException("Menu, " + this + " has not been opened by " + p.getName());

        MenuCloseEvent event = new MenuCloseEvent(p, this);
        if(!event.callEvent()) return event;
        OPEN_MENUS.remove(p.getUniqueId());
        if(closeAction != null) closeAction.close(p);
        slots.values().forEach(slot -> slot.onMenuClose(p));
        return event;
    }

    /**
     * Closes all viewer's inventories.
     */
    public Menu close(){
        for(HumanEntity viewer : new ArrayList<>(getInventory().getViewers())){
            viewer.closeInventory();
        }
        return this;
    }

    public Map<Integer, MenuClick> getClickActions() {
        return clickActions;
    }

    public @Nullable MenuClick getGeneralClickAction() {
        return generalClickAction;
    }

    public @Nullable MenuClick getGeneralInvClickAction() {
        return generalInvClickAction;
    }

    public @Nullable MenuDrag getGeneralDragAction() {
        return generalDragAction;
    }

    public @Nullable MenuOpen getOpenAction() {
        return openAction;
    }

    public @Nullable MenuClose getCloseAction() {
        return closeAction;
    }

    public @NotNull UUID getUuid() {
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

    public Menu setItem(int index, @Nullable ItemStack item){
        return setItem(index, item, false);
    }

    public Menu setItem(int index, @Nullable ItemStack item, boolean silent){
        inventory.setItem(index, item);
        if(!silent) onUpdate();
        return this;
    }
    public Menu setItem(int index, @Nullable ItemStack item, @Nullable MenuClick action){
        return setItem(index, item, action, false);
    }

    public Menu setItem(int index, @Nullable ItemStack item, @Nullable MenuClick action, boolean silent){
        inventory.setItem(index, item);
        setAction(index, action);
        if(!silent) onUpdate();
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

    public @Nullable Slot getSlot(int index){
        return slots.get(index);
    }

    @Override
    public @NotNull Inventory getInventory(){ return inventory; }

    protected Menu addSlot(@NotNull Slot slot){
        return addSlot(slot, true);
    }

    protected Menu addSlot(@NotNull Slot slot, boolean setBlank){
        slots.put(slot.getIndex(), slot);
        if(setBlank) setItem(slot.getIndex(), slot.getSlottedItemReplacement(), true);
        return this;
    }

    protected @Nullable Slot removeSlot(int index){
        return slots.remove(index);
    }

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

    public void onUpdate(){
        updateSlots();
    }

    public void updateSlots(){
        slots.values().forEach(Slot::onMenuUpdate);
    }

    public @NotNull SlotResult takeFromSlot(@NotNull HumanEntity p, @NotNull Slot slot, int amountToTake){
        return takeFromSlot(p, slot, amountToTake, false);
    }

    public @NotNull SlotResult takeFromSlot(@NotNull HumanEntity p, @NotNull Slot slot, int amountToTake, boolean skipUpdate){
        ItemStack slotItem = getInventory().getItem(slot.getIndex());
        if(slotItem == null || slot.isBlank(slotItem)) return new SlotResult(null, false);
        if(!slot.mayTake(p, slotItem)) return new SlotResult(null, false);

        MenuSlotTakeEvent event = new MenuSlotTakeEvent(p, slot, amountToTake);
        if(!event.callEvent() || event.getAmount() < 1){
            return new SlotResult(null, false);
        }
        amountToTake = Math.min(slotItem.getAmount(), event.getAmount());
        if(amountToTake < 1) return new SlotResult(null, false);

        ItemStack clone = slotItem.clone();

        ItemStack newItemClone = slotItem.clone();
        newItemClone.setAmount(newItemClone.getAmount()-amountToTake);
        if(newItemClone.getAmount() < 1) newItemClone = slot.getSlottedItemReplacement();
        final ItemStack oldItemClone = slotItem.clone();

        SlotContext ctx = new SlotContext(newItemClone, oldItemClone);
        slot.onChanged(ctx);
        if(ctx.isCancelled()) return new SlotResult(null, false);

        clone.setAmount(amountToTake);
        slotItem.setAmount(slotItem.getAmount()-amountToTake);
        if(slotItem.getAmount() < 1) slot.setItem(slot.getSlottedItemReplacement(), true);
        if(!skipUpdate) onUpdate();
        return new SlotResult(clone, true);
    }

    public @NotNull SlotResult swapSlot(@NotNull HumanEntity p, @NotNull Slot slot, @Nullable ItemStack item){
        return swapSlot(p, slot, item, false);
    }

    public @NotNull SlotResult swapSlot(@NotNull HumanEntity p, @NotNull Slot slot, @Nullable ItemStack item, boolean skipUpdate){
        ItemStack slotItem = getInventory().getItem(slot.getIndex());
        boolean isSlotted = slot.isSlottedItem(slotItem);
        if(!slot.mayPlace(p, item) || (!isSlotted && !slot.mayTake(p, slotItem))) return new SlotResult(item, false);

        if(item != null && item.getAmount() > slot.getMaxStackSize(item)) return new SlotResult(item, false);
        setItem(slot.getIndex(), CruxItem.isEmpty(item)?slot.getSlottedItemReplacement():item, true);
        if(!skipUpdate) onUpdate();
        return new SlotResult(isSlotted ? null : slotItem, true);
    }

    public @NotNull SlotResult giveSlot(@NotNull HumanEntity p, @NotNull Slot slot, @NotNull ItemStack item, int amount){
        return giveSlot(p, slot, item, amount, false);
    }

    public @NotNull SlotResult giveSlot(@NotNull HumanEntity p, @NotNull Slot slot, @NotNull ItemStack item, int amount, boolean skipUpdate){
        int maxStack = slot.getMaxStackSize(item);
        ItemStack slotItem = getInventory().getItem(slot.getIndex());
        boolean isEmptyOrSlotted = slot.isBlank(slotItem);
        if(!isEmptyOrSlotted){
            if(slotItem.getAmount() >= maxStack) return new SlotResult(item, false);
            if(!slotItem.isSimilar(item)) return new SlotResult(item, false);
        }
        if(!slot.mayPlace(p, item)) return new SlotResult(item, false);

        MenuSlotGiveEvent event = new MenuSlotGiveEvent(p, slot, amount);
        if(!event.callEvent() || event.getAmount() < 1){
            return new SlotResult(item, false);
        }
        amount = event.getAmount();

        //how much more can this item hold
        int maxGiveAmount = isEmptyOrSlotted ? maxStack : (maxStack-slotItem.getAmount());

        //how much to actually give the item
        int amountToGive = Math.min(amount, maxGiveAmount);
        ItemStack newItemClone;
        final ItemStack oldItemClone = slotItem == null ? null : slotItem.clone();
        if(isEmptyOrSlotted){
            ItemStack set = item.clone();
            set.setAmount(amountToGive);

            newItemClone = set;

            SlotContext ctx = new SlotContext(newItemClone, oldItemClone);
            slot.onChanged(ctx);
            if(ctx.isCancelled()) return new SlotResult(item, false);

            setItem(slot.getIndex(), set);
            item.setAmount(item.getAmount()-amountToGive);
            if(!skipUpdate) onUpdate();
            return new SlotResult(item, true);
        }

        newItemClone = slotItem.clone();
        newItemClone.setAmount(newItemClone.getAmount()+amountToGive);
        SlotContext ctx = new SlotContext(newItemClone, oldItemClone);
        slot.onChanged(ctx);
        if(ctx.isCancelled()) return new SlotResult(item, false);

        slotItem.setAmount(slotItem.getAmount()+amountToGive);
        item.setAmount(item.getAmount()-amountToGive);
        if(!skipUpdate) onUpdate();
        return new SlotResult(item, true);
    }

    public static class SlotResult{
        protected final ItemStack item;
        protected final boolean result;

        public SlotResult(ItemStack item, boolean result) {
            this.item = item;
            this.result = result;
        }

        public ItemStack getItem() {
            return item;
        }

        public boolean getResult() {
            return result;
        }
    }

    public void onInvClick(@NotNull InventoryClickEvent event){
        event.setCancelled(false);
        ClickType clickType = event.getClick();
        if(clickType == ClickType.DOUBLE_CLICK){
            event.setCancelled(true);
            ItemStack item = event.getCursor();
            if(CruxItem.isEmpty(item) || item.getAmount() >= CruxItem.getMaxStackSize(item)) return;
            HumanEntity p = event.getWhoClicked();

            //check their own inventory first
            for(ItemStack inv : p.getInventory().getStorageContents()){
                if(!item.isSimilar(inv) || inv == null) continue;
                int amountToTake = Math.min(
                    CruxItem.getMaxStackSize(item) - item.getAmount(), inv.getAmount()
                );
                if(amountToTake < 1) continue;

                item.setAmount(item.getAmount()+amountToTake);
                inv.setAmount(inv.getAmount()-amountToTake);
            }

            int amountToTake = CruxItem.getMaxStackSize(item) - item.getAmount();
            if(amountToTake < 1) return;
            for(Slot slot : slots.values()){
                if(slot.isSlottedItem(slot.getItem()) || !slot.mayTake(p, slot.getItem())) continue;
                if(!item.isSimilar(slot.getItem())) continue;
                amountToTake = CruxItem.getMaxStackSize(item) - item.getAmount();
                if(amountToTake < 1) return;

                SlotResult took = takeFromSlot(p, slot, amountToTake, true);
                if(!took.getResult() || took.getItem() == null) continue;
                item.setAmount(item.getAmount()+took.getItem().getAmount());
                onUpdate();
            }
            return;
        }

        if(!event.isShiftClick()) return;
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if(CruxItem.isEmpty(item)) return;
        HumanEntity p = event.getWhoClicked();

        for(Slot slot : slots.values()){
            if(!slot.isSlottedItem(slot.getItem()) && !slot.mayPlace(p, item)) continue;
            giveSlot(p, slot, item, item.getAmount());
        }
    }

    public void onDrag(@NotNull InventoryDragEvent event){
        if(event.getRawSlots().size() < 2){
            for(int i : event.getRawSlots()){
                if(i >= event.getView().getTopInventory().getSize()){
                    event.setCancelled(false);
                    return;
                }
                Slot slot = getSlot(i);
                if(slot==null) return;
                Crux.getServer().getScheduler().runTask(Crux.getMainPlugin(), task ->{
                    HumanEntity p = event.getWhoClicked();
                    SlotResult result = giveSlot(p, slot, event.getOldCursor(), event.getOldCursor().getAmount(), true);
                    if(!result.getResult()) return;
                    p.setItemOnCursor(result.getItem());
                    onUpdate();
                });
                return;
            }
        }
        for(int s : event.getRawSlots()){
            if(s < event.getView().getTopInventory().getSize()) return;
        }
        event.setCancelled(false);
    }

    public void onMenuClick(@NotNull InventoryClickEvent event){
        Slot slot = getSlot(event.getSlot());
        if(slot==null) return;
        HumanEntity p = event.getWhoClicked();
        slot.onClick(p, event);

        ItemStack clicked = getInventory().getItem(slot.getIndex());

        if(event.isShiftClick()){
            int takeAmount = clicked == null ? 0 : clicked.getAmount();
            if(takeAmount < 1) return;
            if(p.getInventory().firstEmpty() < 0) return;
            SlotResult result = takeFromSlot(p, slot, takeAmount, true);
            if(!result.getResult() || result.getItem() == null) return;
            p.getInventory().addItem(result.getItem());
            onUpdate();
            return;
        }

        ItemStack cursor = event.getCursor();

        ClickType clickType = event.getClick();

        int hotBar = event.getHotbarButton();
        if(hotBar > -1){
            cursor = p.getInventory().getItem(hotBar);
            SlotResult result = swapSlot(p, slot, cursor, true);
            if(!result.getResult()) return;
            p.getInventory().setItem(hotBar, result.getItem());
            onUpdate();
            return;
        }

        if(CruxItem.isEmpty(cursor)){
            if(CruxItem.isEmpty(clicked)) return;
            int takeAmount = clickType.isRightClick() ? clicked.getAmount()/2 : clicked.getAmount();
            SlotResult result = takeFromSlot(p, slot, takeAmount, true);
            if(!result.getResult()) return;
            p.setItemOnCursor(result.getItem());
            onUpdate();
            return;
        }

        if(clicked != null && !clicked.isSimilar(cursor) && !slot.isBlank(clicked)){
            SlotResult result = swapSlot(p, slot, cursor, true);
            if(!result.getResult()) return;
            p.setItemOnCursor(result.getItem());
            return;
        }

        int giveAmount = clickType.isRightClick() ? 1 : cursor.getAmount();
        giveSlot(p, slot, cursor, giveAmount);
    }
}
