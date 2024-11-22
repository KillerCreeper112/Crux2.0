package killercreepr.crux.api.component.parser.persistent;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.core.component.parser.persistent.SimpleComponentInputField;
import killercreepr.crux.core.component.parser.type.ComponentParserTypes;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.persistence.type.ListTagType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface ComponentInputField<T> {
    static <T> ComponentInputField<T> createList(@NotNull ComponentInputField<?> value, @NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            new ListTagType<>(value.dataType()),
            ComponentTextInputParser.list(value.textInputParser()),
            field
        );
    }
    static <T> ComponentInputField<T> create(@NotNull ComponentInputField<?> value, @NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            value.dataType(),
            value.textInputParser(),
            field
        );
    }
    static <T> ComponentInputField<T> create(@NotNull PersistentTextParser<?> value, @NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            value.dataType(),
            value,
            field
        );
    }
    static <T> ComponentInputField<T> createList(@NotNull PersistentTextParser<?> value, @NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            new ListTagType<>(value.dataType()),
            ComponentTextInputParser.list(value),
            field
        );
    }
    static <T> ComponentInputField<T> createFloat(@NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            PersistentDataType.FLOAT,
            ComponentParserTypes.FLOAT,
            field
        );
    }
    static <T> ComponentInputField<T> createBool(@NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            PersistentDataType.BOOLEAN,
            ComponentParserTypes.BOOLEAN,
            field
        );
    }
    static <T> ComponentInputField<T> createInt(@NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            PersistentDataType.INTEGER,
            ComponentParserTypes.INTEGER,
            field
        );
    }
    static <T> ComponentInputField<T> createDouble(@NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            PersistentDataType.DOUBLE,
            ComponentParserTypes.DOUBLE,
            field
        );
    }
    static <T> ComponentInputField<T> createString(@NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            PersistentDataType.STRING,
            ComponentParserTypes.STRING,
            field
        );
    }
    static <T> ComponentInputField<T> createKey(@NotNull Function<T, Object> field){
        return new SimpleComponentInputField<>(
            CruxPersistence.CRUX_KEY,
            ComponentParserTypes.KEY,
            field
        );
    }

    @NotNull
    PersistentDataType<?, ?> dataType();
    @NotNull
    ComponentTextInputParser<?> textInputParser();
    @NotNull
    Function<T, Object> field();

    interface Builder<T>{
        Builder<T> dataType(PersistentDataType<?, ?> dataType);
        Builder<T> textInputParser(ComponentTextInputParser<?> textInputParser);
        Builder<T> field(Function<T, Object> field);
        ComponentInputField<T> build();
    }
}
