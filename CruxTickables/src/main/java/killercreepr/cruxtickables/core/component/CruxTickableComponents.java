package killercreepr.cruxtickables.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;

import java.util.function.UnaryOperator;

public class CruxTickableComponents {
    public static void register(){}
    /*public static final DataComponentType<CruxAttributeContainer> CRUX_ATTRIBUTES = register("crux_attributes", builder -> builder
        .persistTextParser(CruxAttributeCompParsers.CRUX_ATTRIBUTES.createInput(Crux.key("crux_attributes"))));*/

    public static final DataComponentType<EntityTickablesContainer> ENTITY_TICKABLES = register(
        "entity_tickables", builder -> builder
            .persistTextParser(CruxTickableCompParsers.ENTITY_TICKABLES_CONTAINER.createInput(Crux.key("entity_tickables")))
    );

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
    private static <T> DataComponentType<T> register(String id, DataComponentType<T> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator);
    }
}
