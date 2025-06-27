package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;
import killercreepr.cruxworlds.core.registries.WorldsRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.Objects;

public class CruxWorldsParsers {
    public static PersistTextParser<CruxWorldType> WORLD_TYPE = PersistTextParser.elementBuilder(CruxWorldType.class)
        .field(TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return Objects.requireNonNull(
                WorldsRegistries.WORLD_TYPE.get(key),
                "CruxWorldType of " + key + " not found!"
            );
        });
    public static PersistTextParser<LootTable<NaturalEntitySpawnGroup>> NATURAL_ENTITY_SPAWN_GROUP = PersistTextParser.elementBuilder((Class) LootTable.class)
        .field(TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return Objects.requireNonNull(
                WorldsRegistries.NATURAL_ENTITY_SPAWN_GROUP_LOOT_TABLE.get(key),
                "NaturalEntitySpawnGroupLootTable of " + key + " not found!"
            );
        });
}
