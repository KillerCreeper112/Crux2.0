package killercreepr.cruxadvancements.core.advancement.criteria;

import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListCriteria implements CruxCriteria {
    public static ListCriteria listCriteria(List<String[]> requirements){
        List<String> actionNames = new ArrayList<>();
        for(String[] r : requirements){
            for(String s : r){
                if(!actionNames.contains(s)) actionNames.add(s);
            }
        }

        return new ListCriteria(actionNames.toArray(new String[0]), requirements.toArray(new String[][]{}));
    }
    public static ListCriteria listCriteriaFromList(List<List<String>> requirements){
        List<String> actionNames = new ArrayList<>();
        for(var r : requirements){
            for(String s : r){
                if(!actionNames.contains(s)) actionNames.add(s);
            }
        }

        return new ListCriteria(actionNames.toArray(new String[0]), requirements.toArray(new String[][]{}));
    }

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
