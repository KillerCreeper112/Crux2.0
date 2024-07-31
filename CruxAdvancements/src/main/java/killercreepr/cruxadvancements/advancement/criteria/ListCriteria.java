package killercreepr.cruxadvancements.advancement.criteria;

import org.jetbrains.annotations.NotNull;

public class ListCriteria implements CruxCriteria {
    protected final @NotNull String[][] requirements;

    public ListCriteria(@NotNull String[][] requirements) {
        this.requirements = requirements;
    }

    public @NotNull String[][] getRequirements() {
        return requirements;
    }
}
