package killercreepr.cruxcrafting.api.event;

import killercreepr.cruxcrafting.api.crafting.CruxRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EntityDiscoverRecipeEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected final Entity entity;
    protected final CruxRecipeManager<?> recipeManager;
    protected final CruxRecipe recipe;
    protected boolean cancel = false;

    public EntityDiscoverRecipeEvent(Entity entity, CruxRecipeManager<?> recipeManager, CruxRecipe recipe) {
        this.entity = entity;
        this.recipeManager = recipeManager;
        this.recipe = recipe;
    }

    public CruxRecipe getRecipe() {
        return recipe;
    }

    public CruxRecipeManager<?> getRecipeManager() {
        return recipeManager;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
