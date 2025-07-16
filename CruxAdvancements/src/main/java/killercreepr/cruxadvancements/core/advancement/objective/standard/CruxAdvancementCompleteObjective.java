package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxadvancements.api.event.CruxAdvancementGrantEvent;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.core.loot.context.AdvanceLootCtx;
import org.jetbrains.annotations.NotNull;

public class CruxAdvancementCompleteObjective extends GenericEventObjective<CruxAdvancementGrantEvent> {
    public CruxAdvancementCompleteObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(CruxAdvancementGrantEvent event) {
        return AdvanceLootCtx.builder(event).build();
    }
}
