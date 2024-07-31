package killercreepr.cruxadvancements.event;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.AdvancementManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAdvancementEvent extends PlayerEvent {
    protected final @NotNull AdvancementManager manager;
    protected final @NotNull CruxAdvancement advancement;
    public AbstractAdvancementEvent(@NotNull Player who, @NotNull AdvancementManager manager, @NotNull CruxAdvancement advancement) {
        super(who);
        this.manager = manager;
        this.advancement = advancement;
    }

    public @NotNull AdvancementManager getManager() {
        return manager;
    }

    public @NotNull CruxAdvancement getAdvancement() {
        return advancement;
    }
}
