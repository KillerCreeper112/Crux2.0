package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxworlds.world.creator.CruxWorldType;

import java.util.function.UnaryOperator;

public class CruxWorldsComponents {
    public static void register(){}
    public static final DataComponentType<CruxWorldType> WORLD_TYPE = register("world_type", builder -> builder
        .persistTextParser(CruxWorldsParsers.WORLD_TYPE.createInput(Crux.key("world_type"))));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
