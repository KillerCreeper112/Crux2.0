package killercreepr.cruxadvancements.core.command.argument;

import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

public interface CruxAdvancementResolver {
    @NotNull
    CruxAdvancement resolve(@NotNull CruxAdvancementManager<?> manager);
}
