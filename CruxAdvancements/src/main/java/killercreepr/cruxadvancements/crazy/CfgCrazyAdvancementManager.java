package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CfgCrazyAdvancementManager extends CrazyAdvancementManager<CrazyAdvancement> {
    public CfgCrazyAdvancementManager(@NotNull Key key, @NotNull AdvancementManager crazyManager) {
        super(key, crazyManager);
    }

    @Override
    public void save(@NotNull Plugin plugin) {

    }

    @Override
    public void load(@NotNull Plugin plugin) {

    }
}
