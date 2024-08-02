package killercreepr.cruxadvancements.event;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CruxAdvancementProgressChangeEvent extends AbstractAdvancementEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected int newProgress;
    public CruxAdvancementProgressChangeEvent(@NotNull UUID who,
                                              @NotNull CruxAdvancementManager<?> manager,
                                              @NotNull CruxAdvancement advancement, int newProgress) {
        super(who, manager, advancement);
        this.newProgress = newProgress;
    }

    public void setNewProgress(int newProgress) {
        this.newProgress = newProgress;
    }

    public int getNewProgress() {
        return newProgress;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
