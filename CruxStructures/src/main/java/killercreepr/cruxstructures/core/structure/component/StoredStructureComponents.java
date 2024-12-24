package killercreepr.cruxstructures.core.structure.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxstructures.api.component.StoredBlocks;
import org.bukkit.util.BoundingBox;

import java.util.function.UnaryOperator;

public class StoredStructureComponents {
    public static void register(){}
    public static final DataComponentType<BoundingBox> OUTER_BOX = register("outer_box",builder -> builder);
    public static final DataComponentType<StoredBlocks> STORE_BLOCKS = register("store_blocks", builder -> builder);
    public static final DataComponentType<Boolean> DISABLE_BLOCK_BREAK = register("disable_block_break", builder -> builder);
    public static final DataComponentType<Boolean> DISABLE_BLOCK_PLACE = register("disable_block_place", builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key("stored_structure/" + id), builderOperator.apply(DataComponentType.builder()).build());
    }
}