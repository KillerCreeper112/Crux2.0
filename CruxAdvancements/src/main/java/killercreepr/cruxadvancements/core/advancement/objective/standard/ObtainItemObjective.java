package killercreepr.cruxadvancements.core.advancement.objective.standard;

import killercreepr.cruxadvancements.core.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import org.jetbrains.annotations.NotNull;

public class ObtainItemObjective extends NumberObjective {
    public ObtainItemObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data, maxProgress);
    }
}
