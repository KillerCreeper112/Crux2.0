package killercreepr.cruxentities.handler;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.crux.api.handler.EntityHandler;
import killercreepr.crux.core.entity.BukkitEntitySnapshot;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.entity.CruxMobSnapshot;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxEntitiesEntityHandler implements EntityHandler {
    @Override
    public @NotNull Key getType(@NotNull Entity entity) {
        Key key = CruxMob.getKey(entity);
        if(key != null) return key;
        return entity.getType().key();
    }

    @Override
    public @Nullable CruxEntitySnapshot createEntitySnapshot(@NotNull Key type) {
        return createEntitySnapshot(type, null);
    }

    @Override
    public @Nullable CruxEntitySnapshot createEntitySnapshot(@NotNull Key type, @Nullable DataComponentAccessor components) {
        CruxMob mob = CruxEntityRegistries.ENTITIES.get(type);
        if(mob == null){
            EntityType t = Registry.ENTITY_TYPE.get(type);
            if(t == null) return null;
            return new BukkitEntitySnapshot(t, components);
        }
        return new CruxMobSnapshot(mob, components);
    }
}
