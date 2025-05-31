package killercreepr.cruxblocks.api.block.active;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ActiveCruxEntityMove {
    void onEntityMove(@NotNull Entity e);
}
