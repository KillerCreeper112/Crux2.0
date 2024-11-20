package killercreepr.cruxmenus.api.menu.slot;

import killercreepr.crux.core.util.CruxItem;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.contex.SlotContext;
import killercreepr.cruxmenus.core.menu.slot.SimpleFixedSlot;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface Slot {
    static @NotNull Slot click(@NotNull Menu menu, int index, @NotNull Consumer<InventoryClickEvent> click){
        return new SimpleFixedSlot(menu, index){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                click.accept(event);
            }
        };
    }

    @NotNull Menu getMenu();
    default @Nullable ItemStack getItem(){
        return getMenu().getInventory().getItem(getIndex());
    }

    default Slot setItem(@Nullable ItemStack item){
        return setItem(item, false);
    }
    default Slot setItem(@Nullable ItemStack item, boolean silent){
        getMenu().setItem(getIndex(), item, silent);
        return this;
    }
    default void onMenuUpdate(){
    }
    int getIndex();

    default void onChanged(@NotNull SlotContext ctx){

    }

    default void onMenuOpen(@NotNull Player p){

    }

    default void onMenuClose(@NotNull Player p){

    }

    default @Nullable Integer getMaxStackSize(){
        return null;
    }
    default boolean isBlank(@Nullable ItemStack item){
        return CruxItem.isEmpty(item) || isSlottedItem(item);
    }
    default int getMaxStackSize(@Nullable ItemStack item) {
        Integer maxStack = this.getMaxStackSize();
        if(maxStack==null) return item==null?0:CruxItem.getMaxStackSize(item);
        return Math.min(maxStack, item==null?0:CruxItem.getMaxStackSize(item));
    }

    default boolean isSlottedItem(@Nullable ItemStack item){
        ItemStack replacement = getSlottedItemReplacement();
        return replacement != null && replacement.isSimilar(item);
    }

    default @Nullable ItemStack getSlottedItemReplacement(){
        return null;
    }

    default void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event){
    }

    default boolean mayPlace(@NotNull HumanEntity p, @Nullable ItemStack item){
        return true;
    }

    default boolean mayTake(@NotNull HumanEntity p, @Nullable ItemStack item){
        return true;
    }
}
