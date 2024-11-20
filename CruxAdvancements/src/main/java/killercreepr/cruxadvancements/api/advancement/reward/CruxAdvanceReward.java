package killercreepr.cruxadvancements.api.advancement.reward;

import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface CruxAdvanceReward {
    void onReward(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement, @NotNull UUID who);

    interface Online extends CruxAdvanceReward{
        @Override
        default void onReward(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement, @NotNull UUID who){
            Player online = Crux.getServer().getPlayer(who);
            if(online==null) return;
            onReward(manager, advancement, online);
        }

        void onReward(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement, @NotNull Player p);
    }
}
