package killercreepr.crux.api.component.parser.persistent;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.serialization.PersistentDataSerializer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.persistent.AlternativePersistTextParser;
import killercreepr.crux.core.component.parser.persistent.MappedPersistentComponentInputParser;
import killercreepr.crux.core.component.parser.persistent.PrimitivePersistentComponentInputParser;
import killercreepr.crux.core.component.parser.persistent.SinglePersistentComponentInputParser;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface PersistentTextParser<T> extends ComponentTextInputParser<T>, PersistentDataSerializer<T> {
    static PersistentTextParser<Boolean> createBool(@NotNull Key key){
        return primitive(key, ComponentInputField.createBool(e -> e));
    }
    static PersistentTextParser<String> createString(@NotNull Key key){
        return primitive(key, ComponentInputField.createString(e -> e));
    }
    static PersistentTextParser<Float> createFloat(@NotNull Key key){
        return primitive(key, ComponentInputField.createFloat(e -> e));
    }
    static PersistentTextParser<Double> createDouble(@NotNull Key key){
        return primitive(key, ComponentInputField.createDouble(e -> e));
    }

    ///
    static PersistentTextParser<Boolean> createBool(@NotNull String key){
        return createBool(Crux.key(key));
    }
    static PersistentTextParser<String> createString(@NotNull String key){
        return createString(Crux.key(key));
    }
    static PersistentTextParser<Float> createFloat(@NotNull String key){
        return createFloat(Crux.key(key));
    }
    static PersistentTextParser<Double> createDouble(@NotNull String key){
        return createDouble(Crux.key(key));
    }

    static <T> MapBuilder<T> mapBuilder(@NotNull Class<T> type){
        return new MappedPersistentComponentInputParser.Builder<>(type);
    }

    static <T> AlternativeBuilder<T> alternativeBuilder(@NotNull Class<T> type){
        return new AlternativePersistTextParser.Builder<>();
    }

    static <T> SingleBuilder<T> singleBuilder(@NotNull Class<T> type){
        return new SinglePersistentComponentInputParser.Builder<>();
    }

    static <T> PersistentTextParser<T> primitive(@NotNull String id, ComponentInputField<T> field){
        return primitive(Crux.key(id), field);
    }

    static <T> PersistentTextParser<T> primitive(@NotNull Key id, ComponentInputField<T> field){
        return new PrimitivePersistentComponentInputParser<>(id, field);
    }

    interface AlternativeBuilder<T>{
        AlternativeBuilder<T> add(@NotNull PersistentTextParser<T> parser);
        PersistentTextParser<T> finish(@NotNull PersistentTextParser<T> parser);
        PersistentTextParser<T> build();
    }

    interface SingleBuilder<T>{
        //protected final @NotNull Key key;
        //    protected final @NotNull ComponentInputField<T> field;
        //    protected final @NotNull Function<ComponentParseContext, T> output;

        SingleBuilder<T> field(String id, ComponentInputField<T> field);
        SingleBuilder<T> field(Key id, ComponentInputField<T> field);
        SingleBuilder<T> output(Function<ComponentParseContext, T> output);
        PersistentTextParser<T> apply(Function<ComponentParseContext, T> output);

        PersistentTextParser<T> build();
    }

    interface MapBuilder<T>{
        default MapBuilder<T> key(String key){
            return key(Crux.key(key));
        }
        MapBuilder<T> key(Key key);
        MapBuilder<T> dataType(PersistentDataType<?, T> dataType);
        MapBuilder<T> field(String id, ComponentInputField<T> field);

        MapBuilder<T> output(Function<ComponentParseContext, T> output);

        PersistentTextParser<T> apply(Function<ComponentParseContext, T> output);

        PersistentTextParser<T> build();
    }
}
