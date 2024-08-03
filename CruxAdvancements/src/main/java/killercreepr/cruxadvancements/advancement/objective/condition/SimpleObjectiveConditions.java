package killercreepr.cruxadvancements.advancement.objective.condition;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class SimpleObjectiveConditions implements ObjectiveConditions {
    protected final @NotNull Collection<ObjectiveCondition> conditions;

    public SimpleObjectiveConditions(@NotNull Collection<ObjectiveCondition> conditions) {
        this.conditions = Collections.unmodifiableCollection(conditions);
    }

    @Override
    public boolean test(@NotNull ConditionContext ctx) {
        for(ObjectiveCondition c : conditions){
            if(!c.test(ctx)) return false;
        }
        return true;
    }
}
