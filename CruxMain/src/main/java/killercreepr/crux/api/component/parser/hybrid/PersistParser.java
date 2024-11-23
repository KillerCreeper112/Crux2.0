package killercreepr.crux.api.component.parser.hybrid;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.serialization.PersistentDataSerializer;

public interface PersistParser<T> extends ComponentTextInputParser<T>, PersistentDataSerializer<T> {

}
