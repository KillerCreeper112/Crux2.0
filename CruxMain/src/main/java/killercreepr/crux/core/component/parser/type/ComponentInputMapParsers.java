package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import net.kyori.adventure.key.Key;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.Collection;
import java.util.Map;

public class ComponentInputMapParsers {
    public final PersistTextParser<Map<Key, Double>> KEY_DOUBLE_MAP = PersistTextParser.mapDynamicBuilder((Class<Map<Key, Double>>) (Class) Map.class)
        .keyParser(PersistTextParser.KEY)
        .valueParser(PersistTextParser.DOUBLE)
        .mapToEncode(m -> m)
        .apply(InputDecodeContext::get);
    public final PersistTextParser<Map<Key, Integer>> KEY_INT_MAP = PersistTextParser.mapDynamicBuilder((Class<Map<Key, Integer>>) (Class) Map.class)
        .keyParser(PersistTextParser.KEY)
        .valueParser(PersistTextParser.INTEGER)
        .mapToEncode(m -> m)
        .apply(InputDecodeContext::get);
    public final PersistTextParser<Map<Key, Float>> KEY_FLOAT_MAP = PersistTextParser.mapDynamicBuilder((Class<Map<Key, Float>>) (Class) Map.class)
        .keyParser(PersistTextParser.KEY)
        .valueParser(PersistTextParser.FLOAT)
        .mapToEncode(m -> m)
        .apply(InputDecodeContext::get);

    public final PersistTextParser<Map<Attribute, Collection<AttributeModifier>>> ATTRIBUTE_MAP = PersistTextParser.mapDynamicBuilder((Class<Map<Attribute, Collection<AttributeModifier>>>) (Class) Map.class)
        .keyParser(ComponentInputParsers.ATTRIBUTE)
        .valueParser(ComponentInputParsers.LIST.ATTRIBUTE_MODIFIER)
        .mapToEncode(m -> m)
        .apply(InputDecodeContext::get);
}
