package killercreepr.cruxitems.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxitems.api.item.component.InteractableComponent;

import java.util.List;
import java.util.function.UnaryOperator;

public class CruxItemsComponents {
    public static void register(){}

    public static final DataComponentType<List<InteractableComponent>> INTERACTABLES = register("interactables", builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
