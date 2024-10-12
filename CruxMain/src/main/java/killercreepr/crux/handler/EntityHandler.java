package killercreepr.crux.handler;

import killercreepr.crux.entity.BukkitEntitySnapshot;
import killercreepr.crux.entity.CruxEntitySnapshot;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityHandler {
    @NotNull
    Key getType(@NotNull Entity entity);
    @Nullable
    CruxEntitySnapshot createEntitySnapshot(@NotNull Key type);

    class Dummy implements EntityHandler{
        @Override
        public @NotNull Key getType(@NotNull Entity entity) {
            return entity.getType().key();
        }

        @Override
        public @Nullable CruxEntitySnapshot createEntitySnapshot(@NotNull Key type) {
            EntityType t = Registry.ENTITY_TYPE.get(type);
            if(t == null) return null;
            return new BukkitEntitySnapshot(t);
        }
    }
}
