package killercreepr.crux.component;

import killercreepr.crux.Crux;
import killercreepr.crux.component.serialzation.PersistentDataSerializer;
import killercreepr.crux.item.component.ToolComponent;
import killercreepr.crux.persistence.CruxPersist;
import killercreepr.crux.persistence.CruxPersistence;
import killercreepr.crux.registries.CruxRegistries;

import java.util.function.UnaryOperator;

public class CruxComponents {
    public static void register(){}

    public static final DataComponentType<Float> HARDNESS = register("hardness", builder -> builder);
    public static final DataComponentType<Boolean> UNBREAKABLE = register("unbreakable", builder -> builder);
    public static final DataComponentType<ToolComponent> TOOL = register("tool",
        builder -> builder.persistent(PersistentDataSerializer.create(Crux.key(CruxPersist.TOOL.tagName()), CruxPersistence.TOOL_COMPONENT)));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
