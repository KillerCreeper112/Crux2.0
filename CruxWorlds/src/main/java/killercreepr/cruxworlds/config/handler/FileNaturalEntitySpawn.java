package killercreepr.cruxworlds.config.handler;

import killercreepr.crux.entity.CruxEntitySnapshot;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxworlds.config.entity.CfgNaturalEntitySpawn;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.world.spawning.SolidGroundSpawnValidator;
import killercreepr.cruxworlds.world.spawning.SpawnValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            return new CfgNaturalEntitySpawn(1, 0f, snapshot, new SolidGroundSpawnValidator());
        }
        CruxEntitySnapshot snapshot = registry.deserializeFromFile(CruxEntitySnapshot.class, o.get("entity"));
        if(snapshot == null) return null;
        int weight = o.getObject(Integer.class, "weight", 1);
        float quality = o.getObject(Float.class, "quality", 0f);
        SpawnValidator validator = registry.deserializeFromFile(SpawnValidator.class, o.get("spawn_conditions"));
        return new CfgNaturalEntitySpawn(weight, quality, snapshot, validator);
    }
}
