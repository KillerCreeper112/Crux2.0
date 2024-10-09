package killercreepr.cruxadvancements.event.objective;

import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.event.AbstractAdvancementEvent;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class AbstractObjectiveEvent extends AbstractAdvancementEvent {
    protected final @NotNull ObjectiveAdvancement objectiveAdvancement;
    public AbstractObjectiveEvent(@NotNull UUID who, @NotNull CruxAdvancementManager<?> manager, @NotNull ObjectiveAdvancement advancement) {
        super(who, manager, advancement);
        this.objectiveAdvancement = advancement;
    }

    @Override
    public @NotNull ObjectiveAdvancement getAdvancement() {
        return objectiveAdvancement;
    }
}
