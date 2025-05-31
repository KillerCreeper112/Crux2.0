package killercreepr.cruxblocks.api.block.active;

import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public interface ActiveCruxEntityDamageOn {
    void onEntityDamage(@NotNull EntityDamageEvent event);
}
