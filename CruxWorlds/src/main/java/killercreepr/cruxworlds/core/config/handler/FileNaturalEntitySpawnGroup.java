package killercreepr.cruxworlds.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import killercreepr.cruxworlds.core.config.entity.CfgKeyedNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.core.config.entity.CfgNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.core.registries.WorldsRegistries;
import killercreepr.cruxworlds.core.world.spawning.SolidGroundSpawnValidator;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.logging.Level;

public class FileNaturalEntitySpawnGroup implements FileObjectHandler<NaturalEntitySpawnGroup> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull NaturalEntitySpawnGroup object) {
        throw new UnsupportedOperationException("unsupported");
    }

    public NaturalEntitySpawnGroup handleReference(@NotNull FileContext<?> ctx, @NotNull FileElement e){
        String keyString;
        if(e instanceof FileObject o){
            keyString = o.getObject(String.class, "key");
        }else if(e.isFilePrimitive()){
            keyString = e.getAsString();
        }else return null;
        if(keyString == null) return null;
        if(keyString.startsWith("#")){
            keyString = keyString.substring(1);
            var x = WorldsRegistries.NATURAL_ENTITY_SPAWN_GROUP.get(Crux.key(keyString));
            if(x == null) Crux.log(Level.SEVERE, "NaturalEntitySpawnGroup of " + keyString + " not found!");
            return x;
        }
        return null;
    }

    @Override
    public @Nullable NaturalEntitySpawnGroup deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        return deserializeFromFile(ctx, e, null);
    }

    public @Nullable NaturalEntitySpawnGroup deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e,
                                                            @Nullable Key key) {
        FileRegistry registry = ctx.getRegistry();
        NaturalEntitySpawnGroup group = handleReference(ctx, e);
        if(group != null) return group;
        if(!(e instanceof FileObject o)) return null;
        if(key == null) key = registry.deserializeFromFile(Key.class, o.get("key"));

        Collection<NaturalEntitySpawn> spawns = registry.deserializeFromFile(
            new TypeToken<Collection<NaturalEntitySpawn>>(){}.getType(),
            o.get("spawns")
        );
        if(spawns == null || spawns.isEmpty()) return null;
        int weight = o.getObject(Integer.class, "weight", 1);
        float quality = o.getObject(Float.class, "quality", 0f);
        SpawnValidator validator = registry.deserializeFromFile(SpawnValidator.class, o.get("spawn_conditions"));
        if(validator == null) validator = new SolidGroundSpawnValidator();
        if(key != null) return new CfgKeyedNaturalEntitySpawnGroup(weight, quality, spawns, validator, key);
        return new CfgNaturalEntitySpawnGroup(weight, quality, spawns, validator);
    }
}
