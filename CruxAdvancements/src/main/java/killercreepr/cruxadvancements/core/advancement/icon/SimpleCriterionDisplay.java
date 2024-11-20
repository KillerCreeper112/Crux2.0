package killercreepr.cruxadvancements.core.advancement.icon;

import killercreepr.cruxadvancements.api.advancement.icon.CriterionDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SimpleCriterionDisplay implements CriterionDisplay {
    protected final @NotNull Map<String, String> map;
    public SimpleCriterionDisplay(@NotNull Map<String, String> map) {
        this.map = map;
    }

    public @NotNull Map<String, String> getMap() {
        return map;
    }

    @Override
    public @Nullable String getCriterionFormat(@NotNull String criterion) {
        return map.get(criterion);
    }

    @Override
    public @NotNull Map<String, String> getFormat() {
        return map;
    }
}
