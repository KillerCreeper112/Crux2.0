package killercreepr.cruxadvancements.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.cruxadvancements.advancement.objective.condition.ObjectiveConditions;
import killercreepr.cruxadvancements.advancement.objective.condition.SimpleObjectiveConditions;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class FileObjectiveConditions implements FileHandler<ObjectiveConditions> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull ObjectiveConditions object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable ObjectiveConditions deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        Collection<LootCondition> objectives = ctx.getRegistry().deserialize(new TypeToken<Collection<LootCondition>>(){}.getType(), e);
        if(objectives==null) return null;
        return new SimpleObjectiveConditions(objectives);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "objective_conditions";
    }
}
