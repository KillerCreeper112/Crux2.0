package killercreepr.cruxblocks.config.handler.component;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.component.TypedDataComponent;
import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.util.CruxDirection;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxblocks.block.component.*;
import killercreepr.cruxblocks.block.standard.component.EntitySpawnerComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawner;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CfgBlockComponents {
    public static void register(@NotNull MappedRegistry<String, FileDataComponentType<?>> registry){
        registry.register("directional_block", new FileDataComponentType<DirectionalBlock>() {
            @Override
            public @Nullable TypedDataComponent<DirectionalBlock> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                BlockFace direction;
                try{
                    direction = ctx.getRegistry().deserializeFromFile(BlockFace.class, e.get("direction"));
                }catch (ClassCastException ignored){ direction = null; }

                if(direction==null){
                    Axis axis = ctx.getRegistry().deserializeFromFile(Axis.class, e.get("direction"));
                    if(axis == null) return null;
                    direction = CruxDirection.getFaceFromAxis(axis);
                }
                return TypedDataComponent.create(
                    CruxBlockComponents.DIRECTIONAL_BLOCK,
                    new DirectionalBlock.Simple(direction)
                );
            }
        });

        registry.register("directional_group", new FileDataComponentType<DirectionalGroup>() {
            @Override
            public @Nullable TypedDataComponent<DirectionalGroup> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxBlockComponents.DIRECTIONAL_GROUP,
                    new DirectionalGroup.Simple(e.getObject(Boolean.class, "orientable", false))
                );
            }
        });

        registry.register("requires_correct_tool_for_drops", new FileDataComponentType<Boolean>() {
            @Override
            public @Nullable TypedDataComponent<Boolean> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxBlockComponents.REQUIRES_CORRECT_TOOL_FOR_DROPS,
                    e.getObject(Boolean.class, "requires_correct_tool_for_drops", false)
                );
            }
        });

        registry.register("block_sound_group", new FileDataComponentType<CreateBlockSoundGroup>() {
            @Override
            public @Nullable TypedDataComponent<CreateBlockSoundGroup> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                FileRegistry registry = ctx.getRegistry();
                CreateBlockSoundGroup soundGroup = registry.deserializeFromFile(CreateBlockSoundGroup.class, e.get("block_sound_group"));
                return TypedDataComponent.create(
                    CruxBlockComponents.BLOCK_SOUND_GROUP,
                    soundGroup
                );
            }
        });

        registry.register("bush_block", new FileDataComponentType<BushBlock>() {
            @Override
            public @Nullable TypedDataComponent<BushBlock> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                BushType bushType = ctx.getRegistry().deserializeFromFile(BushType.class, e.get("bush_type"));
                if(bushType == null) return null;
                return TypedDataComponent.create(
                    CruxBlockComponents.BUSH_BLOCK,
                    new BushBlock.Simple(bushType)
                );
            }
        });

        registry.register("bush_group", new FileDataComponentType<BushGroup>() {
            @Override
            public @Nullable TypedDataComponent<BushGroup> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxBlockComponents.BUSH_GROUP,
                    new BushGroup.Simple()
                );
            }
        });

        registry.register("entity_spawner", new FileDataComponentType<EntitySpawnerComponent>() {
            @Override
            public @Nullable TypedDataComponent<EntitySpawnerComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                FileRegistry registry = ctx.getRegistry();
                Collection<NaturalEntitySpawnGroup> spawns = registry.deserializeFromFile(
                    new TypeToken<Collection<NaturalEntitySpawnGroup>>(){}.getType(), e.get("spawns")
                );
                if(spawns == null || spawns.isEmpty()) return null;

                NumberProvider spawnDelay = num(registry, e, "spawn_delay", NumberProvider.constant(400));
                NumberProvider spawnRange = num(registry, e, "spawn_range", NumberProvider.constant(8));
                NumberProvider spawnCount = num(registry, e, "spawn_count", NumberProvider.uniform(4, 10));
                NumberProvider requiredPlayerRange = num(registry, e, "required_player_range", NumberProvider.constant(16));
                NumberProvider maxSpawnAttempts = num(registry, e, "max_spawn_attempts", NumberProvider.constant(8));
                NumberProvider groupSpawnAmount = num(registry, e, "group_spawn_amount", NumberProvider.constant(1));

                return TypedDataComponent.create(
                    CruxBlockComponents.ENTITY_SPAWNER,
                    new EntitySpawnerComponent(
                        spawnDelay, spawnRange, spawnCount, requiredPlayerRange,
                        maxSpawnAttempts, groupSpawnAmount, spawns
                    )
                );
            }
        });
    }

    private static NumberProvider num(FileRegistry registry, FileObject o, String x, NumberProvider fallback){
        NumberProvider v = registry.deserializeFromFile(NumberProvider.class, o.get(x));
        if(v == null) return fallback;
        return v;
    }
}
