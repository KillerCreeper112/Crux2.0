package killercreepr.cruxadvancements.api.advancement.manager;

import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.crux.api.data.PluginLoadable;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.event.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

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

    default void save(){
        for(T advance : getAdvancements()){
            advance.onSaving(this);
        }
    }

    default void load(){
        for(T advance : getAdvancements()){
            advance.onLoading(this);
        }
    }

    default void saveProgress(@NotNull Player player, @NotNull T... advancements){
        saveProgress(player.getUniqueId(), advancements);
    }
    default void loadProgress(@NotNull Player player, @NotNull T... advancements){
        loadProgress(player.getUniqueId(), advancements);
    }
    default void unloadProgress(@NotNull Player player, @NotNull T... advancements){
        unloadProgress(player.getUniqueId(), advancements);
    }
    void loadAllUserProgress(@NotNull T... advancements);
    void saveAllUserProgress(@NotNull T... advancements);
    void deleteAllUserProgress(@NotNull T... advancements);

    void saveProgress(@NotNull UUID uuid, @NotNull T... advancements);
    void loadProgress(@NotNull UUID uuid, @NotNull T... advancements);
    void unloadProgress(@NotNull UUID uuid, @NotNull T... advancements);

    void registerAdvancement(@NotNull T advancement);
    void unregisterAdvancement(@NotNull T advancement);
    @NotNull
    KeyedRegistry<T> getAdvancements();
    @Nullable T getAdvancement(@NotNull Key key);
    default boolean hasAdvancement(@NotNull Key key){
        AdvancementManager d;
        return getAdvancement(key) != null;
    }

    @Nullable
    CruxAdvancementGrantEvent grantAdvancement(@NotNull UUID uuid, @NotNull T advancement);

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

    @Nullable CruxAdvancementRewardEvent grantAdvancementReward(@NotNull UUID who, @NotNull T advancement);

    //player
    default @Nullable CruxAdvancementGrantEvent grantAdvancement(@NotNull Player who, @NotNull T advancement){
        return grantAdvancement(who.getUniqueId(), advancement);
    }

    default @Nullable
    CruxAdvancementRevokeEvent revokeAdvancement(@NotNull Player who, @NotNull T advancement){
        return revokeAdvancement(who.getUniqueId(), advancement);
    }

    default @Nullable
    CruxAdvancementCriteriaGrantEvent grantCriteria(@NotNull Player who, @NotNull T advancement,
                                                    @NotNull String... criteria){
        return grantCriteria(who.getUniqueId(), advancement, criteria);
    }

    default @Nullable
    CruxAdvancementCriteriaRevokeEvent revokeCriteria(@NotNull Player who, @NotNull T advancement,
                                                      @NotNull String... criteria){
        return revokeCriteria(who.getUniqueId(), advancement, criteria);
    }

    default @Nullable
    CruxAdvancementProgressChangeEvent setCriteriaProgress(@NotNull Player who,
                                                           @NotNull T advancement,
                                                           int newProgress){
        return setCriteriaProgress(who.getUniqueId(), advancement, newProgress);
    }
    default @Nullable CruxAdvancementRewardEvent grantAdvancementReward(@NotNull Player who, @NotNull T advancement){
        return grantAdvancementReward(who.getUniqueId(), advancement);
    }

    default void refresh(@NotNull Plugin plugin){
        refresh(plugin, null);
    }
    void refresh(@NotNull Plugin plugin, @Nullable Consumer<CruxAdvancementManager<?>> loadConsumer);
}
