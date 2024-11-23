package killercreepr.crux.api.component.parser.standard;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import net.kyori.adventure.key.Key;

import java.util.List;

public class ComponentParserListTypeHolder {
    public final PersistTextParser<List<String>> STRING = PersistTextParser.list(PersistTextParser.STRING);
    public final PersistTextParser<List<Integer>> INTEGER = PersistTextParser.list(PersistTextParser.INTEGER);
    public final PersistTextParser<List<Float>> FLOAT = PersistTextParser.list(PersistTextParser.FLOAT);
    public final PersistTextParser<List<Double>> DOUBLE = PersistTextParser.list(PersistTextParser.DOUBLE);
    public final PersistTextParser<List<Key>> KEY = PersistTextParser.list(PersistTextParser.KEY);
    public final PersistTextParser<List<Boolean>> BOOLEAN = PersistTextParser.list(PersistTextParser.BOOLEAN);
}
