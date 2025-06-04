package killercreepr.cruxentities.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.serialization.PersistentDataSerializer;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.type.ComponentInputListParsers;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.registries.CruxRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class CruxEntityComponents {
    public static void register(){}

    public static final DataComponentType<SpawnerData> CREATURE_SPAWNER_DATA = register("creature_spawner_data", builder -> builder
        .persistTextParser(PersistTextParser.mapBuilder(SpawnerData.class)
            .field("mob_map", TextInputField.field(
                PersistTextParser.mapDynamicBuilder((Class<Map< Key, Key>>) (Class) Map.class)
                    .keyParser(PersistTextParser.KEY)
                    .valueParser(PersistTextParser.KEY)
                    .mapToEncode(m -> m)
                    .apply(InputDecodeContext::get), e -> e.getFromVanillaToCustom()
            ))
            .apply(ctx ->{
                return new SpawnerData(
                    ctx.get("mob_map")
                );
            })
            .createInput(Crux.key("creature_spawner_data"))));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
