package killercreepr.cruxmenus.api.menu;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.container.SimpleMergedTagContainer;
import killercreepr.cruxmenus.api.event.MenuCloseEvent;
import killercreepr.cruxmenus.api.event.MenuOpenEvent;
import killercreepr.cruxmenus.api.event.MenuRefreshEvent;
import killercreepr.cruxmenus.api.event.slot.MenuSlotGiveEvent;
import killercreepr.cruxmenus.api.event.slot.MenuSlotTakeEvent;
import killercreepr.cruxmenus.api.menu.contex.SlotContext;
import killercreepr.cruxmenus.api.menu.module.MenuModuleRegistry;
import killercreepr.cruxmenus.api.menu.slot.Slot;
import killercreepr.cruxmenus.core.listener.MenuListener;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Menu extends CommonMenu, InventoryHolder {
    Menu reconstruct(int size, @NotNull Component name);
    Menu reconstruct(@NotNull Inventory inv);
    @NotNull
    MenuOpenEvent open(@NotNull HumanEntity p);
    void openSilently(@NotNull HumanEntity p);
    /**
     * This does not actually close the player's inventory.
     * Use {@link Player#closeInventory()} instead and this method will automatically
     * be called. See {@link MenuListener}
     */
    @NotNull
    MenuCloseEvent close(@NotNull HumanEntity p);
    MenuRefreshEvent refresh();

    default void onRefresh(){
    }

    boolean isRefreshing();

    /**
     * Closes all viewer's inventories.
     */
    default int close(){
        int x = getViewers().size();
        new HashSet<>(getViewers()).forEach(HumanEntity::closeInventory);
        return x;
    }

    @NotNull UUID uuid();

    default void setItem(int index, @Nullable ItemStack item){
        setItem(index, item, false);
    }

    void setItem(int index, @Nullable ItemStack item, boolean silent);
    default void setItem(int index, @Nullable ItemStack item, @Nullable Slot slot){
        setItem(index, item, slot, false);
    }

    default void setItem(int index, @Nullable ItemStack item, @Nullable Slot slot, boolean silent){
        putSlot(index, slot);
        setItem(index, item, silent);
    }

    void putSlot(int index, @Nullable Slot slot);

    default void clearItems(boolean silent){
        getInventory().clear();
        if(!silent) onUpdate();
    }

    /**
     * Called after creation.
     */
    default void load(){
        refresh();
        getModules().load();
    }

    @Nullable Slot getSlot(int index);
    @NotNull Map<Integer, Slot> getSlots();

    int buildSize();

    default @Nullable MergedTagContainer buildTags(@NotNull TagParser tagParser){
        MergedTagContainer tags = new SimpleMergedTagContainer(tagParser);
        tags.addAll(getModules().buildTags(tagParser));
        return tags;
    }
    @NotNull Component buildTitle();
    @NotNull
    MenuModuleRegistry getModules();
    /**
     * Resets the inventory. More namely, clears the items.
     */
    default void reset(){
        clearItems(false);
    }

    default @NotNull List<HumanEntity> getViewers(){
        return getInventory().getViewers();
    }

    default @NotNull SlotResult takeFromSlot(@NotNull HumanEntity p, @NotNull Slot slot, int amountToTake){
        return takeFromSlot(p, slot, amountToTake, false);
    }

    default @NotNull SlotResult takeFromSlot(@NotNull HumanEntity p, @NotNull Slot slot, int amountToTake, boolean skipUpdate){
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

        SlotContext ctx = SlotContext.context(p, newItemClone, oldItemClone);
        slot.onChanged(ctx);
        if(ctx.isCancelled()) return new SlotResult(null, false);

        clone.setAmount(amountToTake);
        slotItem.setAmount(slotItem.getAmount()-amountToTake);
        if(slotItem.getAmount() < 1) slot.setItem(slot.getSlottedItemReplacement(), true);
        if(!skipUpdate) onUpdate();
        return new SlotResult(clone, true);
    }

    default @NotNull SlotResult swapSlot(@NotNull HumanEntity p, @NotNull Slot slot, @Nullable ItemStack item){
        return swapSlot(p, slot, item, false);
    }

    default @NotNull SlotResult swapSlot(@NotNull HumanEntity p, @NotNull Slot slot, @Nullable ItemStack item, boolean skipUpdate){
        ItemStack slotItem = getInventory().getItem(slot.getIndex());
        boolean isSlotted = slot.isSlottedItem(slotItem);
        if(!slot.mayPlace(p, item) || (!isSlotted && !slot.mayTake(p, slotItem))) return new SlotResult(item, false);

        if(item != null && item.getAmount() > slot.getMaxStackSize(item)) return new SlotResult(item, false);
        setItem(slot.getIndex(), CruxItem.isEmpty(item)?slot.getSlottedItemReplacement():item, true);
        if(!skipUpdate) onUpdate();
        return new SlotResult(isSlotted ? null : slotItem, true);
    }

    default @NotNull SlotResult giveSlot(@NotNull HumanEntity p, @NotNull Slot slot, @NotNull ItemStack item, int amount){
        return giveSlot(p, slot, item, amount, false);
    }

    default @NotNull SlotResult giveSlot(@NotNull HumanEntity p, @NotNull Slot slot, @NotNull ItemStack item, int amount, boolean skipUpdate){
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

            SlotContext ctx = SlotContext.context(p, newItemClone, oldItemClone);
            slot.onChanged(ctx);
            if(ctx.isCancelled()) return new SlotResult(item, false);

            setItem(slot.getIndex(), set);
            item.setAmount(item.getAmount()-amountToGive);
            if(!skipUpdate) onUpdate();
            return new SlotResult(item, true);
        }

        newItemClone = slotItem.clone();
        newItemClone.setAmount(newItemClone.getAmount()+amountToGive);
        SlotContext ctx = SlotContext.context(p, newItemClone, oldItemClone);
        slot.onChanged(ctx);
        if(ctx.isCancelled()) return new SlotResult(item, false);

        slotItem.setAmount(slotItem.getAmount()+amountToGive);
        item.setAmount(item.getAmount()-amountToGive);
        if(!skipUpdate) onUpdate();
        return new SlotResult(item, true);
    }

    class SlotResult{
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

    default void onInvClick(@NotNull InventoryClickEvent event){
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
            for(Slot slot : getSlots().values()){
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

        for(Slot slot : getSlots().values()){
            if(!slot.isSlottedItem(slot.getItem()) && !slot.mayPlace(p, item)) continue;
            giveSlot(p, slot, item, item.getAmount());
        }
    }

    default void onDrag(@NotNull InventoryDragEvent event){
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

    default void onMenuClick(@NotNull InventoryClickEvent event){
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

            ItemStack slotItem = slot.getItem();
            if(slot.isBlank(slotItem) && !CruxItem.isEmpty(cursor)){
                SlotResult result = giveSlot(p, slot, cursor, 1);
                if(!result.getResult()) return;
                p.getInventory().setItem(hotBar, result.getItem());
                onUpdate();
                return;
            }

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
            onUpdate();
            return;
        }

        int giveAmount = clickType.isRightClick() ? 1 : cursor.getAmount();
        giveSlot(p, slot, cursor, giveAmount);
    }
}
