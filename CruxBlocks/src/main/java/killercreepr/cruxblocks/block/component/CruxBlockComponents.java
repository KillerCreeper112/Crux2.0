package killercreepr.cruxblocks.block.component;

import killercreepr.crux.component.DataComponentType;
import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.registries.CruxRegistries;

import java.util.function.UnaryOperator;

public class CruxBlockComponents {
    public static final DataComponentType<DirectionalBlock> DIRECTIONAL_BLOCK = register("directional_block", builder -> builder);
    public static final DataComponentType<DirectionalGroup> DIRECTIONAL_GROUP = register("directional_group", builder -> builder);

    public static final DataComponentType<BushBlock> BUSH_BLOCK = register("bush_block", builder -> builder);
    public static final DataComponentType<BushGroup> BUSH_GROUP = register("bush_group", builder -> builder);

    public static final DataComponentType<CreateBlockSoundGroup> BLOCK_SOUND_GROUP = register("block_sound_group", builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(id, builderOperator.apply(DataComponentType.builder()).build());
    }
}
