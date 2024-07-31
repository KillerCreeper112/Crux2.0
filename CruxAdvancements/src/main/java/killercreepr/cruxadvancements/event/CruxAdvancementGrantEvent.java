package killercreepr.cruxadvancements.event;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.AdvancementManager;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CruxAdvancementGrantEvent extends AbstractAdvancementEvent{
    public CruxAdvancementGrantEvent(@NotNull Player who, @NotNull AdvancementManager manager, @NotNull CruxAdvancement advancement) {
        super(who, manager, advancement);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }
}
