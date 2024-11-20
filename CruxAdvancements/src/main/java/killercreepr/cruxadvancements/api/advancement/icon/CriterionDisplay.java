package killercreepr.cruxadvancements.api.advancement.icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface CriterionDisplay {
    @Nullable String getCriterionFormat(@NotNull String criterion);
    @NotNull
    Map<String, String> getFormat();
}
