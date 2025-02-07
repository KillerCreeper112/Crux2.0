package killercreepr.cruxtickables.core.component;

import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.core.component.parser.hybrid.text.MapPersistTextParser;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EntityTickablePersist<T> extends MapPersistTextParser<T> {
    public EntityTickablePersist(@NotNull Map<String, TextInputField<T, ?>> elements, @NotNull TextInputResultParser<T> resultParser, @NotNull PersistentDataType<PersistentDataContainer, T> dataType) {
        super(elements, resultParser, dataType);
    }

    public EntityTickablePersist(@NotNull Map<String, TextInputField<T, ?>> elements, @NotNull TextInputResultParser<T> resultParser, @NotNull Class<T> type) {
        super(elements, resultParser, type);
    }
}
