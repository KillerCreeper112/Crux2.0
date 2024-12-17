package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.cruxworlds.registries.WorldsRegistries;
import killercreepr.cruxworlds.world.creator.CruxWorldType;
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
}
