package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import net.kyori.adventure.key.Key;

import java.util.Map;

public class ComponentInputMapParsers {
    public final PersistTextParser<Map<Key, Double>> KEY_DOUBLE_MAP = PersistTextParser.mapDynamicBuilder((Class<Map<Key, Double>>) (Class) Map.class)
        .keyParser(PersistTextParser.KEY)
        .valueParser(PersistTextParser.DOUBLE)
        .mapToEncode(m -> m)
        .apply(InputDecodeContext::get);
}
