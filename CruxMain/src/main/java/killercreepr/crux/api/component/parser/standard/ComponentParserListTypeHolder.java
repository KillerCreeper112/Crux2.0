package killercreepr.crux.api.component.parser.standard;

import killercreepr.crux.api.component.parser.hybrid.PersistTextInputParser;
import net.kyori.adventure.key.Key;

import java.util.List;

public class ComponentParserListTypeHolder {
    public final PersistTextInputParser<List<String>> STRING = PersistTextInputParser.list(PersistTextInputParser.STRING);
    public final PersistTextInputParser<List<Integer>> INTEGER = PersistTextInputParser.list(PersistTextInputParser.INTEGER);
    public final PersistTextInputParser<List<Float>> FLOAT = PersistTextInputParser.list(PersistTextInputParser.FLOAT);
    public final PersistTextInputParser<List<Double>> DOUBLE = PersistTextInputParser.list(PersistTextInputParser.DOUBLE);
    public final PersistTextInputParser<List<Key>> KEY = PersistTextInputParser.list(PersistTextInputParser.KEY);
    public final PersistTextInputParser<List<Boolean>> BOOLEAN = PersistTextInputParser.list(PersistTextInputParser.BOOLEAN);
}
