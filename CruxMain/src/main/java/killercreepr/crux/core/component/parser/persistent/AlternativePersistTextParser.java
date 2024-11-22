package killercreepr.crux.core.component.parser.persistent;

import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AlternativePersistTextParser<T> implements PersistentTextParser<T> {
    protected final @NotNull Collection<PersistentTextParser<T>> values;
    public AlternativePersistTextParser(@NotNull Collection<PersistentTextParser<T>> values) {
        this.values = values;
    }

    @Override
    public @NotNull Object encodeObject(@NotNull T object){
        for(PersistentTextParser<T> parser : values){
            return parser.encodeObject(object);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        for(PersistentTextParser<T> parser : values){
            try{
                return parser.decodeObject(object);
            }catch (Exception ignored){
                ignored.printStackTrace();
            }
        }
        throw new IllegalArgumentException("Cannot decode object " + object + "! Tried " + values.size() + " parsers.");
    }

    @Override
    public @Nullable T decode(@NotNull PersistentDataContainer from) {
        for(PersistentTextParser<T> parser : values){
            try{
                T value = parser.decode(from);
                if(value == null) continue;
                return value;
            }catch (Exception ignored){}
        }
        return null;
    }

    @Override
    public @Nullable T encode(@NotNull PersistentDataContainer to, @Nullable T value) {
        for(PersistentTextParser<T> parser : values){
            try{
                return parser.encode(to, value);
            }catch (Exception ignored){}
        }
        throw new IllegalStateException("Cannot encode object " + value + "! Tried " + values.size() + " parsers.");
    }

    @Override
    public @NotNull PersistentDataType<?, T> dataType() {
        for(PersistentTextParser<T> parser : values){
            if(parser.dataType() != null) return parser.dataType();
        }
        throw new UnsupportedOperationException("AlternativePersistTextParser does not have a dataType supported!");
    }

    public static class Builder<T> implements PersistentTextParser.AlternativeBuilder<T>{
        protected final @NotNull List<PersistentTextParser<T>> parsers = new ArrayList<>();
        @Override
        public AlternativeBuilder<T> add(@NotNull PersistentTextParser<T> parser) {
            parsers.add(parser);
            return this;
        }

        @Override
        public PersistentTextParser<T> finish(@NotNull PersistentTextParser<T> parser) {
            return add(parser).build();
        }

        @Override
        public PersistentTextParser<T> build() {
            return new AlternativePersistTextParser<>(parsers);
        }
    }
}
