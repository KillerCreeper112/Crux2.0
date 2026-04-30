package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.core.component.parser.hybrid.text.persistence.MapDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MapPersistTextParser<T> implements PersistTextParser<T> {
    protected final @NotNull Map<String, TextInputField<T, ?>> elements;
    protected final @NotNull TextInputResultParser<T> resultParser;
    protected final @NotNull PersistentDataType<PersistentDataContainer, T> dataType;

    public MapPersistTextParser(@NotNull Map<String, TextInputField<T, ?>> elements,
                                @NotNull TextInputResultParser<T> resultParser,
                                @NotNull PersistentDataType<PersistentDataContainer, T> dataType) {
        this.elements = elements;
        this.resultParser = resultParser;
        this.dataType = dataType;
    }

    public MapPersistTextParser(@NotNull Map<String, TextInputField<T, ?>> elements,
                                @NotNull TextInputResultParser<T> resultParser,
                                @NotNull Class<T> type) {
        this.elements = elements;
        this.resultParser = resultParser;
        this.dataType = buildDataType(type, elements);
    }

    public MapPersistTextParser(@NotNull Map<String, TextInputField<T, ?>> elements,
                                @NotNull TextInputResultParser<T> resultParser,
                                @NotNull Function<PersistTextParser<T>, PersistentDataType<PersistentDataContainer, T>> function) {
        this.elements = elements;
        this.resultParser = resultParser;
        this.dataType = function.apply(this);
    }

    public PersistentDataType<PersistentDataContainer, T> buildDataType(@NotNull Class<T> complexType,
                                                                        @NotNull Map<String, TextInputField<T, ?>> elements){
        return new MapDataType<>(complexType, elements, this);
    }

    public @NotNull Map<String, TextInputField<T, ?>> getElements() {
        return elements;
    }

    public @NotNull TextInputResultParser<T> getResultParser() {
        return resultParser;
    }

    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        if(!(object instanceof Map<?,?> map)) throw new IllegalArgumentException(object + " is not Map!");
        TextInputField<T,?> base = elements.getOrDefault("", elements.get(null));
        if(base != null){
            if(base instanceof TextInputField.Holder<T,?> holder){
                var inputParser = holder.getInputParser(object);
                if(inputParser == null){
                    throw new IllegalArgumentException("Input parser is null! " + object);
                }
                return (T) holder.getInputParser(object).decodeObject(object);
            }
            var inputParser = base.inputParser();
            if(inputParser == null){
                throw new IllegalArgumentException("Input parser is null! " + object);
            }
            return (T) inputParser.decodeObject(object);
        }

        Map<String, Object> parsed = new HashMap<>();
        elements.forEach((id, field) ->{
            if(id == null || id.isBlank()){
                return;
            }
            Object value = map.get(id);
            if(value == null) return;
            Object decoded = field.inputParser().decodeObject(value);
            parsed.put(id, decoded);
        });
        InputDecodeContext ctx = InputDecodeContext.context(parsed);
        return resultParser.parse(ctx);
    }

    @Override
    public @NotNull Map<String, Object> encodeObject(@NotNull T object) {
        TextInputField<T,?> base = elements.getOrDefault("", elements.get(null));
        if(base != null){
            if(base instanceof TextInputField.Holder<T,?> holder){
                return (Map<String, Object>) holder.inputParser(object).encodeObjectUnchecked(object);
            }
            return (Map<String, Object>) base.inputParser().encodeObjectUnchecked(object);
        }

        Map<String, Object> map = new HashMap<>();
        elements.forEach((id, field) ->{
            if(id == null || id.isBlank()){
                return;
            }

            Object parsedField = field.parseField(object);
            if(parsedField == null) return;
            Object encoded = field.inputParser().encodeObjectUnchecked(parsedField);
            map.put(id, encoded);
        });
        return map;
    }

    @Override
    public @NotNull PersistentDataType<?, T> dataType() {
        return dataType;
    }

    public static class Builder<T> implements PersistTextParser.MapBuilder<T> {
        protected final Map<String, TextInputField<T, ?>> elements = new HashMap<>();
        protected TextInputResultParser<T> resultParser;
        protected PersistentDataType<PersistentDataContainer, T> dataType;
        protected Class<T> dataTypeClass;
        protected Function<PersistTextParser<T>, PersistentDataType<PersistentDataContainer, T>> function;

        @Override
        public MapBuilder<T> field(String name, TextInputField<T, ?> field) {
            elements.put(name, field);
            return this;
        }

        @Override
        public MapBuilder<T> resultParser(TextInputResultParser<T> resultParser) {
            this.resultParser = resultParser;
            return this;
        }

        @Override
        public PersistTextParser<T> apply(TextInputResultParser<T> resultParser) {
            return resultParser(resultParser).build();
        }

        @Override
        public MapBuilder<T> dataType(PersistentDataType<PersistentDataContainer, T> dataType) {
            this.dataType = dataType;
            return this;
        }

        @Override
        public MapBuilder<T> dataTypeClass(Class<T> type) {
            this.dataTypeClass = type;
            return this;
        }

        @Override
        public PersistTextParser<T> build() {
            if(function != null){
                return new MapPersistTextParser<>(elements, resultParser, function);
            }
            if(dataType == null) return new MapPersistTextParser<>(elements, resultParser, dataTypeClass);
            return new MapPersistTextParser<>(elements, resultParser, dataType);
        }

        @Override
        public PersistTextParser<T> buildUnset() {
            if(dataType == null) return new UnsetMapPersistTextParser<>(elements, resultParser, dataTypeClass);
            return new UnsetMapPersistTextParser<>(elements, resultParser, dataType);
        }

        @Override
        public MapBuilder<T> dataTypeFunction(Function<PersistTextParser<T>, PersistentDataType<PersistentDataContainer, T>> function) {
            this.function = function;
            return this;
        }
    }
}
