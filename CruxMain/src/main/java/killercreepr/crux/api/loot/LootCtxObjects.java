package killercreepr.crux.api.loot;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import org.bukkit.Location;

import java.util.Random;
import java.util.function.UnaryOperator;

public class LootCtxObjects {
    public static void register(){}

    public static final DataComponentType<Random> RANDOM = register("random", builder -> builder);
    public static final DataComponentType<Float> LUCK = register("luck", builder -> builder);
    public static final DataComponentType<Object> LOOTER = register("looter", builder -> builder);
    public static final DataComponentType<Object> LOOTED = register("looted", builder -> builder);
    public static final DataComponentType<Location> LOCATION = register("location", builder -> builder);

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key("loot/" + id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
