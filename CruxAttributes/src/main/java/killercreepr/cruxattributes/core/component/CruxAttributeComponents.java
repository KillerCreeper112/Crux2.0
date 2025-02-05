package killercreepr.cruxattributes.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;

import java.util.function.UnaryOperator;

public class CruxAttributeComponents {
    public static void register(){}
    public static final DataComponentType<CruxAttributeContainer> CRUX_ATTRIBUTES = register("crux_attributes", builder -> builder
        .persistTextParser(CruxAttributeCompParsers.CRUX_ATTRIBUTES.createInput(Crux.key("crux_attributes"))));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
