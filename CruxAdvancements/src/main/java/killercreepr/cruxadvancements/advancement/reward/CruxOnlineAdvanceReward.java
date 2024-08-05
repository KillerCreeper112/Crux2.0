package killercreepr.cruxadvancements.advancement.reward;

import killercreepr.crux.Crux;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface CruxOnlineAdvanceReward extends CruxAdvanceReward{
    @Override
    default void onReward(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement, @NotNull UUID who){
        Player online = Crux.getServer().getPlayer(who);
        if(online==null) return;
        onReward(manager, advancement, online);
    }

    void onReward(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement, @NotNull Player p);
}
