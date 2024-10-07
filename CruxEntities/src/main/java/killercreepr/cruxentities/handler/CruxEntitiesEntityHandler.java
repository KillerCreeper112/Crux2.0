package killercreepr.cruxentities.handler;

import killercreepr.crux.handler.EntityHandler;
import killercreepr.cruxentities.entity.CruxMob;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CruxEntitiesEntityHandler implements EntityHandler {
    @Override
    public @NotNull Key getType(@NotNull Entity entity) {
        Key key = CruxMob.getKey(entity);
        if(key != null) return key;
        return entity.getType().key();
    }
}
