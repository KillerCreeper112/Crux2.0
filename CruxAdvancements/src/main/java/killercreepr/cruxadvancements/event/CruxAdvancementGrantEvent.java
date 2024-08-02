package killercreepr.cruxadvancements.event;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CruxAdvancementGrantEvent extends AbstractAdvancementEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    public CruxAdvancementGrantEvent(@NotNull UUID who, @NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement) {
        super(who, manager, advancement);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
