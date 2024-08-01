package killercreepr.cruxadvancements.manager;

import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.crux.data.PluginLoadable;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.event.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface CruxAdvancementManager<T extends CruxAdvancement> extends Keyed, Iterable<T>, PluginLoadable {
    default void registerAdvancement(@NotNull T... advancements){
        for(T a : advancements){
            registerAdvancement(a);
        }
    }
    default void unregisterAdvancement(@NotNull T... advancements){
        for(T a : advancements){
            unregisterAdvancement(a);
        }
    }

    void registerAdvancement(@NotNull T advancement);
    void unregisterAdvancement(@NotNull T advancement);
    @NotNull
    KeyedRegistry<T> getAdvancements();
    @Nullable T getAdvancement(@NotNull Key key);
    default boolean hasAdvancement(@NotNull Key key){
        AdvancementManager d;
        return getAdvancement(key) != null;
    }

    @Nullable CruxAdvancementGrantEvent grantAdvancement(@NotNull UUID uuid, @NotNull T advancement);

    @Nullable
    CruxAdvancementRevokeEvent revokeAdvancement(@NotNull UUID who, @NotNull T advancement);

    @Nullable
    CruxAdvancementCriteriaGrantEvent grantCriteria(@NotNull UUID who, @NotNull T advancement,
                                                    @NotNull String... criteria);

    @Nullable
    CruxAdvancementCriteriaRevokeEvent revokeCriteria(@NotNull UUID who, @NotNull T advancement,
                                                      @NotNull String... criteria);

    @Nullable
    CruxAdvancementProgressChangeEvent setCriteriaProgress(@NotNull UUID who,
                                                           @NotNull T advancement,
                                                           int newProgress);
}
