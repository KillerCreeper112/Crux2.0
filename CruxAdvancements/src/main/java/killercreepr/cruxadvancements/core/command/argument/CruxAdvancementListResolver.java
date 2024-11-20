package killercreepr.cruxadvancements.core.command.argument;

import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CruxAdvancementListResolver {
    @NotNull
    List<CruxAdvancement> resolve(@NotNull CruxAdvancementManager<?> manager);
}
