package killercreepr.crux.api.handler;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.crux.core.entity.BukkitEntitySnapshot;
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
    @Nullable
    CruxEntitySnapshot createEntitySnapshot(@NotNull Key type, @Nullable DataComponentAccessor components);

    class Dummy implements EntityHandler{
        @Override
        public @NotNull Key getType(@NotNull Entity entity) {
            return entity.getType().key();
        }

        @Override
        public @Nullable CruxEntitySnapshot createEntitySnapshot(@NotNull Key type) {
            return createEntitySnapshot(type, null);
        }

        @Override
        public @Nullable CruxEntitySnapshot createEntitySnapshot(@NotNull Key type, @Nullable DataComponentAccessor components) {
            EntityType t = Registry.ENTITY_TYPE.get(type);
            if(t == null) return null;
            return new BukkitEntitySnapshot(t, components);
        }
    }
}
