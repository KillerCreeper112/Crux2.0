package killercreepr.cruxadvancements.manager;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.event.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface CruxAdvancementManager extends Keyed, Iterable<CruxAdvancement> {
    @NotNull
    Map<Key, CruxAdvancement> getAdvancements();
    @Nullable CruxAdvancement getAdvancement(@NotNull Key key);
    default boolean hasAdvancement(@NotNull Key key){
        return getAdvancement(key) != null;
    }

    @Nullable CruxAdvancementGrantEvent grantAdvancement(@NotNull UUID uuid, @NotNull CruxAdvancement advancement);

    @Nullable
    CruxAdvancementRevokeEvent revokeAdvancement(@NotNull UUID who, @NotNull CruxAdvancement advancement);

    @Nullable
    CruxAdvancementCriteriaGrantEvent grantCriteria(@NotNull UUID who, @NotNull CruxAdvancement advancement,
                                                    @NotNull String... criteria);

    @Nullable
    CruxAdvancementCriteriaRevokeEvent revokeCriteria(@NotNull UUID who, @NotNull CruxAdvancement advancement,
                                                      @NotNull String... criteria);

    @Nullable
    CruxAdvancementProgressChangeEvent setCriteriaProgress(@NotNull UUID who,
                                                           @NotNull CruxAdvancement advancement,
                                                           int newProgress);
}
