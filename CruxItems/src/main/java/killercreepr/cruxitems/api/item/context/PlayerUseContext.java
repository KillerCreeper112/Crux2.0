package killercreepr.cruxitems.api.item.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerUseContext {
    @NotNull
    Player getPlayer();
}
