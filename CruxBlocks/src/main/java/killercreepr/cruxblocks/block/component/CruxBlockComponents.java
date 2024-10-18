package killercreepr.cruxblocks.block.component;

import killercreepr.crux.Crux;
import killercreepr.crux.component.DataComponentType;
import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.block.standard.component.EntitySpawnerComponent;

import java.util.function.UnaryOperator;

public class CruxBlockComponents {
    public static final DataComponentType<DirectionalBlock> DIRECTIONAL_BLOCK = register("directional_block", builder ->
        builder);
    public static final DataComponentType<DirectionalGroup> DIRECTIONAL_GROUP = register("directional_group", builder ->
        builder);

    public static final DataComponentType<BushBlock> BUSH_BLOCK = register("bush_block", builder ->
        builder);
    public static final DataComponentType<BushGroup> BUSH_GROUP = register("bush_group", builder ->
        builder);

    public static final DataComponentType<CreateBlockSoundGroup> BLOCK_SOUND_GROUP = register("block_sound_group", builder -> builder);

    public static final DataComponentType<Boolean> REQUIRES_CORRECT_TOOL_FOR_DROPS = register("requires_correct_tool_for_drops",
        builder -> builder);
    public static final DataComponentType<Float> EXPLOSION_RESISTANCE = register("explosion_resistance",
        builder -> builder);
    public static final DataComponentType<Boolean> PISTON_IMMOVABLE = register("piston_immovable",
        builder -> builder);
    public static final DataComponentType<EntitySpawnerComponent> ENTITY_SPAWNER = register("entity_spawner",
        builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
