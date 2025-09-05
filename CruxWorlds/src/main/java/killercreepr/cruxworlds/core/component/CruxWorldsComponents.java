package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;

import java.util.function.UnaryOperator;

public class CruxWorldsComponents {
    public static void register(){}
    public static final DataComponentType<CruxWorldType> WORLD_TYPE = register("world_type", builder -> builder
        .persistTextParser(CruxWorldsParsers.WORLD_TYPE.createInput(Crux.key("world_type"))));

    public static final DataComponentType<Long> WORLD_CREATED_AT_TIME = register("world/created_at_time", builder -> builder
        .persistTextParser(PersistTextParser.LONG.createInput(Crux.key("world/created_at_time"))));

    public static final DataComponentType<EntitySpawnPassengers> ENTITY_SPAWN_PASSENGERS = register("entity_spawn/passengers", builder -> builder);

    public static final DataComponentType<EntitySpawnAttributes> ENTITY_SPAWN_ATTRIBUTES = register("entity_spawn/attributes", builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
