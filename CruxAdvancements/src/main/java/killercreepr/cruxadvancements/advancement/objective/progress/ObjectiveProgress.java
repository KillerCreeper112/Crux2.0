package killercreepr.cruxadvancements.advancement.objective.progress;

import org.jetbrains.annotations.NotNull;

public interface ObjectiveProgress {
    default <T extends ObjectiveProgress> T toType(@NotNull Class<T> type){
        if(!type.isAssignableFrom(this.getClass())){
            throw new IllegalStateException(this + " cannot be cast to " + type);
        }
        return type.cast(this);
    }
}
