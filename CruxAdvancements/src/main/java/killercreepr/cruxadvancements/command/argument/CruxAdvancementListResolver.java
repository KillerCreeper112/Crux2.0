package killercreepr.cruxadvancements.command.argument;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CruxAdvancementListResolver {
    @NotNull
    List<CruxAdvancement> resolve(@NotNull CruxAdvancementManager<?> manager);
}
