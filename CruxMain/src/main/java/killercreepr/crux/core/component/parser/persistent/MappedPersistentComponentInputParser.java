package killercreepr.crux.core.component.parser.persistent;

import killercreepr.crux.api.component.parser.persistent.ComponentInputField;
import killercreepr.crux.api.component.parser.persistent.ComponentParseContext;
import killercreepr.crux.api.component.parser.persistent.PersistentComponentInputParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxTag;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MappedPersistentComponentInputParser<T> implements PersistentComponentInputParser<T> {
    protected final @NotNull Key key;
    protected final @NotNull Map<String, ComponentInputField<T>> inputParsers;
    protected final @NotNull Function<ComponentParseContext, T> output;

    public MappedPersistentComponentInputParser(@NotNull Key key,
                                                @NotNull Map<String, ComponentInputField<T>> inputParsers,
                                                @NotNull Function<ComponentParseContext, T> output) {
        this.key = key;
        this.inputParsers = inputParsers;
        this.output = output;
    }

    public @NotNull Key getKey() {
        return key;
    }

    public @NotNull Function<ComponentParseContext, T> getOutput() {
        return output;
    }

    public @NotNull Map<String, ComponentInputField<T>> getInputParsers() {
        return inputParsers;
    }


    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        if(!(object instanceof Map<?,?> map)) throw new IllegalArgumentException("Object is not a map! " + object);
        ComponentParseContext ctx = new SimpleComponentParseContext<>(this, map);
        return output.apply(ctx);
    }

    @Override
    public @Nullable T decode(@NotNull PersistentDataContainer from) {
        PersistentDataContainer c = CruxTag.get(from, key, PersistentDataType.TAG_CONTAINER, null);
        if(c == null) return null;
        Map<String, Object> map = new HashMap<>();
        for(NamespacedKey key : c.getKeys()){
            PersistentDataType<?, ?> type = inputParsers.get(key.value()).dataType();
            if(!c.has(key, type)) continue;
            Object value = CruxTag.get(c, key, type);
            map.put(key.value(), value);
        }
        return decodeObject(map);
    }

    public @NotNull Map<String, Object> encodeFromObject(@NotNull T object){
        Map<String, Object> map = new HashMap<>();
        inputParsers.forEach((id, parser) ->{
            map.put(id, parser.textInputParser().encodeObjectUnchecked(parser.field().apply(object)));
        });
        return map;
    }

    @Override
    public @Nullable T encode(@NotNull PersistentDataContainer to, @Nullable T value) {
        T previousValue = decode(to);

        PersistentDataContainer c = to.getAdapterContext().newPersistentDataContainer();

        Map<String, Object> map = encodeFromObject(value);

        map.forEach((id, v) ->{
            ComponentInputField<T> field = inputParsers.get(id);
            PersistentDataType<?, Object> dataType = (PersistentDataType<?, Object>) field.dataType();
            CruxTag.set(c, Crux.key(id), dataType, v);
        });

        CruxTag.set(to, key, PersistentDataType.TAG_CONTAINER, c);
        return previousValue;
    }

    public static class Builder<T> implements MapBuilder<T> {
        protected Key key;
        protected final @NotNull Map<String, ComponentInputField<T>> inputParsers = new HashMap<>();
        protected Function<ComponentParseContext, T> output;
        @Override
        public Builder<T> field(String id, ComponentInputField<T> field){
            inputParsers.put(id, field);
            return this;
        }
        @Override
        public Builder<T> output(Function<ComponentParseContext, T> output){
            this.output = output;
            return this;
        }
        @Override
        public PersistentComponentInputParser<T> apply(Function<ComponentParseContext, T> output){;
            return output(output).build();
        }

        @Override
        public PersistentComponentInputParser<T> build() {
            return new MappedPersistentComponentInputParser<>(
                key, inputParsers, output
            );
        }
    }
}
