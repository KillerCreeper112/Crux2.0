package killercreepr.cruxworlds.core.config.handler;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import killercreepr.cruxworlds.core.config.entity.CfgNaturalEntitySpawn;
import killercreepr.cruxworlds.core.world.spawning.SolidGroundSpawnValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class FileNaturalEntitySpawn implements FileObjectHandler<NaturalEntitySpawn> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull NaturalEntitySpawn object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable NaturalEntitySpawn deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        if(!(e instanceof FileObject o)){
            CruxEntitySnapshot snapshot = registry.deserializeFromFile(CruxEntitySnapshot.class, e);
            if(snapshot == null) return null;
            return new CfgNaturalEntitySpawn(1, 0f, snapshot, new SolidGroundSpawnValidator(), DataExchange.empty(),
                DataComponentHandler.simple(Set.of()));
        }

        CruxEntitySnapshot snapshot = registry.deserializeFromFile(CruxEntitySnapshot.class, o.get("entity"));
        if(snapshot == null) return null;
        int weight = o.getObject(Integer.class, "weight", 1);
        float quality = o.getObject(Float.class, "quality", 0f);
        SpawnValidator validator = registry.deserializeFromFile(SpawnValidator.class, o.get("spawn_conditions"));
        @Deprecated
        Boolean persistent = o.getObject(Boolean.class, "persistent");
        @Deprecated
        Boolean removeWhenFar = o.getObject(Boolean.class, "remove_when_far");
        DataExchange data = registry.deserializeFromFile(DataExchange.class, o.get("data"));
        if(data == null){
            var builder = DataExchange.builder();
            if(persistent != null) builder.put("persistent", persistent);
            if(removeWhenFar != null) builder.put("remove_when_far", removeWhenFar);
            data = builder.build();
        }

        DataComponentHandler components = registry.deserializeFromFileOrDefault(DataComponentHandler.class, o.get("components"),
            DataComponentHandler.simple(Set.of()));
        return new CfgNaturalEntitySpawn(weight, quality, snapshot, validator, data, components);
    }
}
