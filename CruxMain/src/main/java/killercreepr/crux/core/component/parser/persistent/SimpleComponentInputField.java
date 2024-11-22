package killercreepr.crux.core.component.parser.persistent;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.parser.persistent.ComponentInputField;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SimpleComponentInputField<T> implements ComponentInputField<T> {
    protected final @NotNull PersistentDataType<?, ?> dataType;
    protected final @NotNull ComponentTextInputParser<?> textInputParser;
    protected final @NotNull Function<T, Object> field;

    public SimpleComponentInputField(@NotNull PersistentDataType<?, ?> dataType,
                                     @NotNull ComponentTextInputParser<?> textInputParser,
                                     @NotNull Function<T, Object> field) {
        this.dataType = dataType;
        this.textInputParser = textInputParser;
        this.field = field;
    }

    @Override
    public @NotNull PersistentDataType<?, ?> dataType() {
        return dataType;
    }

    @Override
    public @NotNull ComponentTextInputParser<?> textInputParser() {
        return textInputParser;
    }

    @Override
    public @NotNull Function<T, Object> field() {
        return field;
    }
}
