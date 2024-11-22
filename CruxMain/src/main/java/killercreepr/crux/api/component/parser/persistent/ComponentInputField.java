package killercreepr.crux.api.component.parser.persistent;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface ComponentInputField<T> {
    @NotNull
    PersistentDataType<?, ?> dataType();
    @NotNull
    ComponentTextInputParser<?> textInputParser();
    @NotNull
    Function<T, Object> field();
}
