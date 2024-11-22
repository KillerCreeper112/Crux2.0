package killercreepr.crux.core.component.parser.persistent;

import killercreepr.crux.api.component.parser.persistent.ComponentInputField;
import killercreepr.crux.api.component.parser.persistent.ComponentParseContext;
import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SinglePersistentComponentInputParser<T> implements PersistentTextParser<T> {
    protected final @NotNull Key key;
    protected final @NotNull ComponentInputField<T> field;
    protected final @NotNull Function<ComponentParseContext, T> output;

    public SinglePersistentComponentInputParser(@NotNull Key key,
                                                @NotNull ComponentInputField<T> field,
                                                @NotNull Function<ComponentParseContext, T> output) {
        this.key = key;
        this.field = field;
        this.output = output;
    }


    public @NotNull Key getKey() {
        return key;
    }


    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        ComponentParseContext ctx = new SimpleComponentParseContext<>(this, field.textInputParser().decodeObject(object));
        return output.apply(ctx);
    }

    @Override
    public @Nullable T decode(@NotNull PersistentDataContainer from) {
        Object parsed = CruxTag.get(from, key, field.dataType(), null);
        if(parsed == null) return null;
        ComponentParseContext ctx = new SimpleComponentParseContext<>(this, parsed);
        return output.apply(ctx);
    }

    @Override
    public @NotNull Object encodeObject(@NotNull T object) {
        return field.textInputParser().encodeObjectUnchecked(field.field().apply(object));
    }

    @Override
    public @Nullable T encode(@NotNull PersistentDataContainer to, @Nullable T value) {
        T previousValue = decode(to);
        if(value == null){
            CruxTag.remove(to, key);
            return previousValue;
        }
        PersistentDataType<?, Object> dataType = (PersistentDataType<?, Object>) dataType();
        Object map = encodeObject(value);
        CruxTag.set(to, key, dataType, map);
        return previousValue;
    }

    @Override
    public @NotNull PersistentDataType<?, T> dataType() {
        return (PersistentDataType<?, T>) field.dataType();
    }

    public static class Builder<T> implements PersistentTextParser.SingleBuilder<T>{
        protected Key key;
        protected ComponentInputField<T> field;
        protected Function<ComponentParseContext, T> output;

        @Override
        public SingleBuilder<T> field(String id, ComponentInputField<T> field) {
            return field(Crux.key(id), field);
        }

        @Override
        public SingleBuilder<T> field(Key id, ComponentInputField<T> field) {
            this.key = id;
            this.field = field;
            return this;
        }

        @Override
        public SingleBuilder<T> output(Function<ComponentParseContext, T> output) {
            this.output = output;
            return this;
        }

        @Override
        public PersistentTextParser<T> apply(Function<ComponentParseContext, T> output) {
            return output(output).build();
        }

        @Override
        public PersistentTextParser<T> build() {
            return new SinglePersistentComponentInputParser<>(key, field, output);
        }
    }
}
