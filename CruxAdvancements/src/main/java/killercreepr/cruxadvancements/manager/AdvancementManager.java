package killercreepr.cruxadvancements.manager;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.event.CruxAdvancementGrantEvent;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AdvancementManager {
    protected final @NotNull Map<Key, CruxAdvancement> advancements = new HashMap<>();
    @NotNull
    public CruxAdvancementGrantEvent grantAdvancement(@NotNull Player p, @NotNull CruxAdvancement advancement){
        CruxAdvancementGrantEvent event = new CruxAdvancementGrantEvent(p, this, advancement);
        return event;
    }
}
