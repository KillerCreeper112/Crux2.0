package killercreepr.cruxadvancements.api.event;

import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class AbstractAdvancementEvent extends Event implements Cancellable {
    protected final @NotNull UUID who;
    protected final @NotNull CruxAdvancementManager<?> manager;
    protected final @NotNull CruxAdvancement advancement;
    protected boolean cancel = false;
    public AbstractAdvancementEvent(@NotNull UUID who, @NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement) {
        super(!Bukkit.isPrimaryThread());
        this.who = who;
        this.manager = manager;
        this.advancement = advancement;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public @NotNull UUID getWho() {
        return who;
    }

    public @NotNull CruxAdvancementManager<?> getManager() {
        return manager;
    }

    public @NotNull CruxAdvancement getAdvancement() {
        return advancement;
    }
}
