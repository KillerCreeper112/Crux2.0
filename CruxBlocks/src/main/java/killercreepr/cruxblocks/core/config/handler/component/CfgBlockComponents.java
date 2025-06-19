package killercreepr.cruxblocks.core.config.handler.component;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.block.CruxBlockWrapper;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.crux.core.util.CruxDirection;
import killercreepr.cruxblocks.api.block.component.*;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.core.block.component.standard.*;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import killercreepr.cruxconfig.config.bukkit.registry.FileDataComponentRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CfgBlockComponents {
    //TODO change this system up probably
    public static void register(@NotNull FileDataComponentRegistry registry){
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

        registry.register("vine_block", new FileDataComponentType<VineBlock>() {
            @Override
            public @Nullable TypedDataComponent<VineBlock> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                VineType bushType = ctx.getRegistry().deserializeFromFile(VineType.class, e.get("vine_type"));
                if(bushType == null) return null;
                return TypedDataComponent.create(
                    CruxBlockComponents.VINE_BLOCK,
                    new VineBlock.Simple(bushType)
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

        registry.register("vine_group", new FileDataComponentType<VineGroup>() {
            @Override
            public @Nullable TypedDataComponent<VineGroup> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxBlockComponents.VINE_GROUP,
                    new VineGroup.Simple()
                );
            }
        });

        registry.register("entity_spawner", new FileEntitySpawnerComponent<EntitySpawnerComponent>() {
            @Override
            public EntitySpawnerComponent createSpawner(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return createGenericSpawner(ctx, e);
            }

            @Override
            public DataComponentType<EntitySpawnerComponent> componentType() {
                return CruxBlockComponents.ENTITY_SPAWNER;
            }
        });

        registry.register("explosion_resistance", new FileDataComponentType<Float>() {
            @Override
            public @Nullable TypedDataComponent<Float> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxBlockComponents.EXPLOSION_RESISTANCE,
                    e.getObject(Float.class, "explosion_resistance", 0f)
                );
            }
        });

        registry.register("piston_immovable", new FileDataComponentType<Boolean>() {
            @Override
            public @Nullable TypedDataComponent<Boolean> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxBlockComponents.PISTON_IMMOVABLE,
                    e.getObject(Boolean.class, "piston_immovable", false)
                );
            }
        });

        registry.register("placeable_check", new FileDataComponentType<PlaceableCheckComponent>() {
            @Override
            public @Nullable TypedDataComponent<PlaceableCheckComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                if(!(e.get("filter") instanceof FileArray a)) return null;
                Map<BlockPos, BlockPredicate> map = new HashMap<>();
                a.forEach(ele ->{
                    if(!(ele instanceof FileObject o)) return;
                    BlockPos pos = ctx.getRegistry().deserializeFromFile(BlockPos.class, o.get("offset"));
                    if(pos==null){
                        Crux.log(Level.WARNING, "Placeable Check component invalid offset! " + e);
                        return;
                    }
                    BlockPredicate predicate = ctx.getRegistry().deserializeFromFile(BlockPredicate.class, o.get("block_predicate"));
                    if(predicate==null){
                        Crux.log(Level.WARNING, "Placeable Check component invalid block predicate! " + e);
                        return;
                    }
                    map.put(pos, predicate);
                });
                if(map.isEmpty()){
                    Crux.log(Level.WARNING, "Placeable Check component does not have any valid filters! " + e);
                    return null;
                }
                return TypedDataComponent.create(
                    CruxBlockComponents.PLACEABLE_CHECK,
                    new PlaceableCheckComponent(map)
                );
            }
        });

        registry.register("interact_harvestable", new FileDataComponentType<InteractHarvestableBlockComponent>() {
            @Override
            public @Nullable TypedDataComponent<InteractHarvestableBlockComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                FileRegistry reg = ctx.getRegistry();
                return TypedDataComponent.create(
                    CruxBlockComponents.INTERACT_HARVESTABLE,
                    new InteractHarvestableBlockComponent(
                        reg.deserializeFromFile(ItemPredicate.class, e.get("item")),
                        reg.deserializeFromFile(String.class, e.get("interact_type")),
                        e.getOrDefaultObject("break_block", false),
                        reg.deserializeFromFile(CruxBlockWrapper.class, e.get("replace_with")),
                        reg.deserializeFromFile(CreateSound.class, e.get("sound")),
                        reg.deserializeFromFile(ItemLootTable.class, e.get("item_drops")),
                        e.getOrDefaultObject("item_damage", 0)
                    )
                );
            }
        });

        registry.register("physical_interact_potion_effects", new FileDataComponentType<CruxInteractablePhysicalBlockComponent>() {
            @Override
            public @Nullable TypedDataComponent<CruxInteractablePhysicalBlockComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                Collection<PotionEffect> potions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<PotionEffect>>(){}.getType(),
                    e.get("potion_effects")
                );
                if(potions == null || potions.isEmpty()) return null;
                EntityPredicate filter = ctx.getRegistry().deserializeFromFile(EntityPredicate.class, e.get("filter"));
                return TypedDataComponent.create(
                    CruxBlockComponents.GENERIC_ENTITY_PHYSICAL_INTERACT,
                    new ApplyPotionEffectsEntityPhysicalInteractComponent(potions, filter)
                );
            }
        });

        registry.register("entity_move_inside_potion_effects", new FileDataComponentType<CruxEntityMoveInsideBlockComponent>() {
            @Override
            public @Nullable TypedDataComponent<CruxEntityMoveInsideBlockComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                Collection<PotionEffect> potions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<PotionEffect>>(){}.getType(),
                    e.get("potion_effects")
                );
                if(potions == null || potions.isEmpty()) return null;
                EntityPredicate filter = ctx.getRegistry().deserializeFromFile(EntityPredicate.class, e.get("filter"));
                return TypedDataComponent.create(
                    CruxBlockComponents.GENERIC_ENTITY_MOVE_INSIDE,
                    new ApplyPotionEffectsEntityMoveInsideBlockComponent(potions, filter)
                );
            }
        });

        registry.register("miner_mine_potion_effects", new FileDataComponentType<CruxMinerMineComponent>() {
            @Override
            public @Nullable TypedDataComponent<CruxMinerMineComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                Collection<PotionEffect> potions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<PotionEffect>>(){}.getType(),
                    e.get("potion_effects")
                );
                if(potions == null || potions.isEmpty()) return null;
                EntityPredicate filter = ctx.getRegistry().deserializeFromFile(EntityPredicate.class, e.get("filter"));
                return TypedDataComponent.create(
                    CruxBlockComponents.GENERIC_MINER_MINE,
                    new ApplyPotionEffectsMinerMIneComponent(potions, filter,
                        e.getOrDefaultObject(Float.class, "mine_speed", null))
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
