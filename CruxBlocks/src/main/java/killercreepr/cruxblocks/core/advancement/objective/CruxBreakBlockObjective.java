package killercreepr.cruxblocks.core.advancement.objective;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.core.advancement.objective.standard.GenericEventObjective;
import killercreepr.cruxblocks.api.event.CruxBlockBreakEvent;
import killercreepr.cruxblocks.core.loot.CruxBlockContexts;
import org.jetbrains.annotations.NotNull;

public class CruxBreakBlockObjective extends GenericEventObjective<CruxBlockBreakEvent> {
    public CruxBreakBlockObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(CruxBlockBreakEvent event) {
        return CruxBlockContexts.builder(event).build();
    }
}
