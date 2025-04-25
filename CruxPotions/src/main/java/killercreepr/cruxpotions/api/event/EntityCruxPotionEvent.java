package killercreepr.cruxpotions.api.event;

import killercreepr.crux.core.Crux;
import killercreepr.cruxpotions.api.potion.ActivePotion;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityCruxPotionEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected boolean cancel;
    protected final @NotNull Entity entity;
    protected final @NotNull ActivePotion effect;
    protected final @Nullable ActivePotion effectToAdd;
    protected final @NotNull Action action;

    public EntityCruxPotionEvent(@NotNull Entity what, @NotNull ActivePotion effect, @Nullable ActivePotion effectToAdd, @NotNull Action action) {
        super(!Crux.isPrimaryThread());
        this.entity = what;
        this.effect = effect;
        this.effectToAdd = effectToAdd;
        this.action = action;
    }

    public @NotNull Entity getEntity() {
        return entity;
    }

    public @NotNull ActivePotion getEffect() {
        return effect;
    }

    public @Nullable ActivePotion getEffectToAdd() {
        return effectToAdd;
    }

    public @NotNull Action getAction() {
        return action;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum Action {

        /**
         * When the potion effect is added because the entity didn't have its
         * type.
         */
        ADDED,
        /**
         * When the entity already had the potion effect type, but the effect is
         * changed.
         */
        CHANGED,
        /**
         * When the potion effect type is completely removed.
         */
        REMOVED,
        CLEARED,
        /**
         * When the potion effect could not be added or changed due to override being
         * false or its amplifier/duration was already higher.
         */
        NONE
    }
}
