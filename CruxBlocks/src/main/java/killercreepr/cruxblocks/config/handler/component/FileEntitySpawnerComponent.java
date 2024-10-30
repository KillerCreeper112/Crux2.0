package killercreepr.cruxblocks.config.handler.component;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.component.DataComponentType;
import killercreepr.crux.component.TypedDataComponent;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxblocks.block.standard.component.EntitySpawnerComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawnGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class FileEntitySpawnerComponent<T extends EntitySpawnerComponent> implements FileDataComponentType<T> {
    @Override
    public @Nullable TypedDataComponent<T> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
        return TypedDataComponent.create(
            componentType(),
            createSpawner(ctx, e)
        );
    }

    public abstract T createSpawner(@NotNull FileContext<?> ctx, @NotNull FileObject e);

    public EntitySpawnerComponent createGenericSpawner(@NotNull FileContext<?> ctx, @NotNull FileObject e){
        FileRegistry registry = ctx.getRegistry();
        Collection<NaturalEntitySpawnGroup> spawns = registry.deserializeFromFile(
            new TypeToken<Collection<NaturalEntitySpawnGroup>>(){}.getType(), e.get("spawns")
        );
        if(spawns == null || spawns.isEmpty()) return null;

        NumberProvider spawnDelay = num(registry, e, "spawn_delay", NumberProvider.uniform(200, 800));
        NumberProvider spawnRange = num(registry, e, "spawn_range", NumberProvider.constant(4));
        NumberProvider innerSpawnDistance = num(registry, e, "inner_spawn_distance", NumberProvider.constant(0));
        NumberProvider spawnCount = num(registry, e, "spawn_count", NumberProvider.constant(4));
        NumberProvider requiredPlayerRange = num(registry, e, "required_player_range", NumberProvider.constant(16));
        NumberProvider maxSpawnAttempts = num(registry, e, "max_spawn_attempts", NumberProvider.constant(8));
        NumberProvider groupSpawnAmount = num(registry, e, "group_spawn_count", NumberProvider.constant(1));
        NumberProvider yCheck = num(registry, e, "y_check", NumberProvider.uniform(1, 3));
        NumberProvider failedDelay = num(registry, e, "failed_delay", NumberProvider.uniform(100, 200));
        boolean ignoreCreativePlayers = e.getObject(Boolean.class, "ignore_creative_players", true);
        return new EntitySpawnerComponent(
            spawnDelay, spawnRange, innerSpawnDistance,
            spawnCount, requiredPlayerRange, maxSpawnAttempts,
            groupSpawnAmount, yCheck,failedDelay, spawns,
            ignoreCreativePlayers
        );
    }

    public abstract DataComponentType<T> componentType();

    private static NumberProvider num(FileRegistry registry, FileObject o, String x, NumberProvider fallback){
        NumberProvider v = registry.deserializeFromFile(NumberProvider.class, o.get(x));
        if(v == null) return fallback;
        return v;
    }
}
