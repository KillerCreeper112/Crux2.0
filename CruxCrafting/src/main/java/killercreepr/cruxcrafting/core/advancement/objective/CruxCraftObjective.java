package killercreepr.cruxcrafting.core.advancement.objective;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.core.advancement.objective.standard.GenericEventObjective;
import killercreepr.cruxcrafting.api.event.PlayerCruxCraftEvent;
import killercreepr.cruxcrafting.core.advancement.CruxCraftLootCtx;
import org.jetbrains.annotations.NotNull;

public class CruxCraftObjective extends GenericEventObjective<PlayerCruxCraftEvent> {
    public CruxCraftObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }

    @Override
    protected LootContext buildContext(PlayerCruxCraftEvent event) {
        return CruxCraftLootCtx.builder(event).build();
    }
}
