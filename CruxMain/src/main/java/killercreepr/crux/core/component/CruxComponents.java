package killercreepr.crux.core.component;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.serialization.PersistentDataSerializer;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.registries.CruxRegistries;

import java.util.function.UnaryOperator;

public class CruxComponents {
    public static void register(){}

    public static final DataComponentType<Float> HARDNESS = register("hardness", builder -> builder
        .textParser(Float.class));
    public static final DataComponentType<Boolean> UNBREAKABLE = register("unbreakable", builder -> builder
        .textParser(Boolean.class));
    public static final DataComponentType<ToolComponent> TOOL = register("tool",
        builder -> builder.persistent(PersistentDataSerializer.create(Crux.key(CruxPersist.TOOL.tagName()), CruxPersistence.TOOL_COMPONENT)));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
