package killercreepr.cruxentities.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.core.component.CruxWorldsComponents;
import killercreepr.cruxworlds.core.component.CruxWorldsParsers;
import net.kyori.adventure.key.Key;

import java.util.Map;
import java.util.function.UnaryOperator;

public class CruxEntityComponents {
    public static void register(){}


    public static final PersistTextParser<SpawnerData.MobData> CREATURE_MOB_DATA = PersistTextParser.mapBuilder(SpawnerData.MobData.class)
        .field("mob", TextInputField.field(PersistTextParser.KEY, e -> e.getMobType()))
        .field("apply_equipment", TextInputField.field(PersistTextParser.BOOLEAN, e -> e.isApplyEquipment()))
        .apply(ctx ->{
            if(!(ctx.get() instanceof Map<?,?>)){
                return new SpawnerData.MobData(ctx.get(), true);
            }
            return new SpawnerData.MobData(
                ctx.get("mob"), ctx.getOptional("apply_equipment", true)
            );
        });

    public static final DataComponentType<SpawnerData> CREATURE_SPAWNER_DATA = register("creature_spawner_data", builder -> builder
        .persistTextParser(PersistTextParser.mapBuilder(SpawnerData.class)
            .field("mob_map", TextInputField.field(
                PersistTextParser.mapDynamicBuilder((Class<Map< Key, SpawnerData.MobData>>) (Class) Map.class)
                    .keyParser(PersistTextParser.KEY)
                    .valueParser(CREATURE_MOB_DATA)
                    .mapToEncode(m -> m)
                    .apply(InputDecodeContext::get), e -> e.getFromVanillaToCustom()
            ))
            .apply(ctx ->{
                return new SpawnerData(
                    ctx.get("mob_map")
                );
            })
            .createInput(Crux.key("creature_spawner_data"))));

    public static final DataComponentType<CreatureSpawnerCfg> CREATURE_SPAWNER_CONFIG = register("creature_spawner_config", builder -> builder
        .persistTextParser(CruxEntityCompParsers.CREATURE_SPAWNER_CFG
            .createInput(Crux.key("creature_spawner_config"))));

    public static final DataComponentType<CreatureSpawnerCfg> OMINOUS_CREATURE_SPAWNER_CONFIG = register("ominous_creature_spawner_config", builder -> builder
        .persistTextParser(CruxEntityCompParsers.CREATURE_SPAWNER_CFG
            .createInput(Crux.key("ominous_creature_spawner_config"))));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
