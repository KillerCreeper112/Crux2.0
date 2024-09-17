package killercreepr.crux.component;

import killercreepr.crux.registries.CruxRegistries;

import java.util.function.UnaryOperator;

public class CruxComponents {
    public static final DataComponentType<Float> HARDNESS = register("hardness", builder -> builder);
    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(id, builderOperator.apply(DataComponentType.builder()).build());
    }
}
