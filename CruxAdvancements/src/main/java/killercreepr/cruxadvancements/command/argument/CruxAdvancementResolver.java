package killercreepr.cruxadvancements.command.argument;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

public interface CruxAdvancementResolver {
    @NotNull
    CruxAdvancement resolve(@NotNull CruxAdvancementManager<?> manager);
}
