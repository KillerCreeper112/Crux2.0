package killercreepr.cruxadvancements.advancement.criteria;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

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

    public boolean test(@NotNull Predicate<String> predicate) {
        if (this.actionNames.length == 0) {
            return false;
        }
        for (String[] list : this.requirements) {
            if (!anyMatch(list, predicate)) {
                return false;
            }
        }

        return true;
    }

    private static boolean anyMatch(String[] requirements, Predicate<String> predicate) {
        for (String string : requirements) {
            if (predicate.test(string)) {
                return true;
            }
        }
        return false;
    }
}
