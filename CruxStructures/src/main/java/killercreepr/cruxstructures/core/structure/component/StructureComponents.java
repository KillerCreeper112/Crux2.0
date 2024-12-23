package killercreepr.cruxstructures.core.structure.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxstructures.api.component.StructureComponent;

import java.util.function.UnaryOperator;

public class StructureComponents {
    public static void register(){}
    public static final DataComponentType<StructureComponent> OUTER_BOX = register("outer_box", builder -> builder);
    public static final DataComponentType<StructureComponent> STORED_BLOCKS = register("stored_blocks", builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key("structure/" + id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
