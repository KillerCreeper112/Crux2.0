package killercreepr.cruxadvancements.event;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CruxAdvancementCriteriaRevokeEvent extends AbstractAdvancementEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected @NotNull String[] criteriaToRevoke;
    public CruxAdvancementCriteriaRevokeEvent(@NotNull UUID who,
                                              @NotNull CruxAdvancementManager<?> manager,
                                              @NotNull CruxAdvancement advancement, @NotNull String[] criteriaToRevoke) {
        super(who, manager, advancement);
        this.criteriaToRevoke = criteriaToRevoke;
    }

    public @NotNull String[] getCriteriaToRevoke() {
        return criteriaToRevoke;
    }

    public void setCriteriaToRevoke(@NotNull String[] criteriaToRevoke) {
        this.criteriaToRevoke = criteriaToRevoke;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
