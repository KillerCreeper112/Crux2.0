package killercreepr.cruxentities.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.cruxworlds.core.component.CruxWorldsParsers;

public class CruxEntityCompParsers {
    public static final PersistTextParser<CreatureSpawnerCfg> CREATURE_SPAWNER_CFG = PersistTextParser
        .mapBuilder(CreatureSpawnerCfg.class)
        .field("spawns", TextInputField.field(CruxWorldsParsers.NATURAL_ENTITY_SPAWN_GROUP, CreatureSpawnerCfg::getSpawns))
        .apply(ctx ->{
            return new CreatureSpawnerCfg(ctx.get("spawns"));
        });
}
