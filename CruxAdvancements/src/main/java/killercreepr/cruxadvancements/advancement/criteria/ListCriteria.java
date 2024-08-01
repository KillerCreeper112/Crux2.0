package killercreepr.cruxadvancements.advancement.criteria;

import org.jetbrains.annotations.NotNull;

public class ListCriteria implements CruxCriteria {
    protected final @NotNull String[] actionNames;
    protected final @NotNull String[][] requirements;

    public ListCriteria(@NotNull String[] actionNames, @NotNull String[][] requirements) {
        this.actionNames = actionNames;
        this.requirements = requirements;
    }

    public @NotNull String[] getActionNames() {
        return actionNames;
    }

    public @NotNull String[][] getRequirements() {
        return requirements;
    }
}
