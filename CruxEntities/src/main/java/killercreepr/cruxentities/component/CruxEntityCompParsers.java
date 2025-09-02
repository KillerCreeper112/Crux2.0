package killercreepr.cruxentities.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;
import killercreepr.cruxworlds.core.component.CruxWorldsParsers;

public class CruxEntityCompParsers {
    public static final PersistTextParser<CreatureSpawnerCfg> CREATURE_SPAWNER_CFG = PersistTextParser
        .mapBuilder(CreatureSpawnerCfg.class)
        .field("spawns", TextInputField.field(CruxWorldsParsers.NATURAL_ENTITY_SPAWN_GROUP, CreatureSpawnerCfg::getSpawns))
        .apply(ctx ->{
            return new CreatureSpawnerCfg(ctx.get("spawns"));
        });
    public static final PersistTextParser<LaunchDrops> LAUNCH_DROPS = PersistTextParser
        .mapBuilder(LaunchDrops.class)
        .field("force", TextInputField.field(ComponentInputParsers.NUMBER_PROVIDER, LaunchDrops::force))
        .field("force_y", TextInputField.field(ComponentInputParsers.NUMBER_PROVIDER, LaunchDrops::yForce))
        .apply(ctx ->{
            return new LaunchDrops(
                ctx.get("force"),
                ctx.get("force_y")
            );
        });
    public static final PersistTextParser<PreventMerge> PREVENT_MERGE = PersistTextParser
        .mapBuilder(PreventMerge.class)
        .field("time", TextInputField.field(PersistTextParser.LONG, PreventMerge::time))
        .field("ticks", TextInputField.field(PersistTextParser.INTEGER, PreventMerge::ticks))
        .apply(ctx ->{
            return new PreventMerge(
                ctx.get("time"),
                ctx.get("ticks")
            );
        });
}
