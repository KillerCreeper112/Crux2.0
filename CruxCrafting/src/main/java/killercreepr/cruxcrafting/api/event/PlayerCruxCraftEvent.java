package killercreepr.cruxcrafting.api.event;

import killercreepr.cruxcrafting.api.crafting.crafter.CruxCraftingCrafter;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerCruxCraftEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected final HumanEntity player;
    protected final CruxRecipe recipe;
    protected final CruxCraftingCrafter crafter;
    protected final List<ItemStack> results;
    protected boolean cancel = false;

    public PlayerCruxCraftEvent(HumanEntity player, CruxRecipe recipe, CruxCraftingCrafter crafter, List<ItemStack> results) {
        this.player = player;
        this.recipe = recipe;
        this.crafter = crafter;
        this.results = results;
    }

    public List<ItemStack> getResults() {
        return results;
    }

    public CruxCraftingCrafter getCrafter() {
        return crafter;
    }

    public CruxRecipe getRecipe() {
        return recipe;
    }

    public HumanEntity getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
