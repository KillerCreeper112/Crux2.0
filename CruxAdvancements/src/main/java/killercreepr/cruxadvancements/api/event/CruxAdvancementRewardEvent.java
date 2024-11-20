package killercreepr.cruxadvancements.api.event;

import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CruxAdvancementRewardEvent extends AbstractAdvancementEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected @NotNull CruxAdvanceReward reward;
    public CruxAdvancementRewardEvent(@NotNull UUID who,
                                      @NotNull CruxAdvancementManager<?> manager,
                                      @NotNull CruxAdvancement advancement,
                                      @NotNull CruxAdvanceReward reward) {
        super(who, manager, advancement);
        this.reward = reward;
    }

    public @NotNull CruxAdvanceReward getReward() {
        return reward;
    }

    public void setReward(@NotNull CruxAdvanceReward reward) {
        this.reward = reward;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
