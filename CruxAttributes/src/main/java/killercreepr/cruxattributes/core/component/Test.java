package killercreepr.cruxattributes.core.component;

import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.core.component.parser.hybrid.text.MapPersistTextParser;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Test extends MapPersistTextParser<CruxAttributeContainer> {
    public Test(@NotNull Map<String, TextInputField<CruxAttributeContainer, ?>> elements, @NotNull TextInputResultParser<CruxAttributeContainer> resultParser, @NotNull PersistentDataType<PersistentDataContainer, CruxAttributeContainer> dataType) {
        super(elements, resultParser, dataType);
    }

    public Test(@NotNull Map<String, TextInputField<CruxAttributeContainer, ?>> elements, @NotNull TextInputResultParser<CruxAttributeContainer> resultParser, @NotNull Class<CruxAttributeContainer> type) {
        super(elements, resultParser, type);
    }
}
