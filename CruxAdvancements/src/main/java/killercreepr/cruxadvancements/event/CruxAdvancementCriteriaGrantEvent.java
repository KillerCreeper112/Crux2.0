package killercreepr.cruxadvancements.event;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.AdvancementManager;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CruxAdvancementCriteriaGrantEvent extends AbstractAdvancementEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected @NotNull String[] criteriaToGrant;
    public CruxAdvancementCriteriaGrantEvent(@NotNull UUID who,
                                             @NotNull AdvancementManager manager,
                                             @NotNull CruxAdvancement advancement, @NotNull String[] criteriaToGrant) {
        super(who, manager, advancement);
        this.criteriaToGrant = criteriaToGrant;
    }

    public @NotNull String[] getCriteriaToGrant() {
        return criteriaToGrant;
    }

    public void setCriteriaToGrant(@NotNull String[] criteriaToGrant) {
        this.criteriaToGrant = criteriaToGrant;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
