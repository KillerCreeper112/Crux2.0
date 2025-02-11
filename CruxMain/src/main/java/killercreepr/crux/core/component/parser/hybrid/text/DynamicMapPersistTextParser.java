package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.core.component.parser.hybrid.text.persistence.DynamicMapDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DynamicMapPersistTextParser<T> extends MapPersistTextParser<T> {
    protected final TextInputField<T, ?> keyField;
    protected final TextInputField<T, ?> valueField;
    protected final Function<T, Map<Object, Object>> encodeMap;
    public DynamicMapPersistTextParser(@NotNull Map<String, TextInputField<T, ?>> elements, @NotNull TextInputResultParser<T> resultParser, @NotNull PersistentDataType<PersistentDataContainer, T> dataType, TextInputField<T, ?> keyField, TextInputField<T, ?> valueField, Function<T, Map<Object, Object>> encodeMap) {
        super(elements, resultParser, dataType);
        this.keyField = keyField;
        this.valueField = valueField;
        this.encodeMap = encodeMap;
    }

    public DynamicMapPersistTextParser(@NotNull Map<String, TextInputField<T, ?>> elements, @NotNull TextInputResultParser<T> resultParser, @NotNull Class<T> type, TextInputField<T, ?> keyField, TextInputField<T, ?> valueField, Function<T, Map<Object, Object>> encodeMap) {
        super(elements, resultParser, type);
        this.keyField = keyField;
        this.valueField = valueField;
        this.encodeMap = encodeMap;
    }

    public DynamicMapPersistTextParser(@NotNull Map<String, TextInputField<T, ?>> elements,
                                @NotNull TextInputResultParser<T> resultParser,
                                @NotNull Function<PersistTextParser<T>, PersistentDataType<PersistentDataContainer, T>> function,
                                TextInputField<T, ?> keyField, TextInputField<T, ?> valueField, Function<T, Map<Object, Object>> encodeMap) {
        super(elements, resultParser, function);
        this.keyField = keyField;
        this.valueField = valueField;
        this.encodeMap = encodeMap;
    }

    public PersistentDataType<PersistentDataContainer, T> buildDataType(@NotNull Class<T> complexType,
                                                                        @NotNull Map<String, TextInputField<T, ?>> elements){
        return new DynamicMapDataType<>(complexType, elements, this);
    }

    public TextInputField<T, ?> getKeyField() {
        return keyField;
    }

    public TextInputField<T, ?> getValueField() {
        return valueField;
    }

    public Function<T, Map<Object, Object>> getEncodeMap() {
        return encodeMap;
    }

    @Override
    public @NotNull Map<String, Object> encodeObject(@NotNull T object) {
        TextInputField<T,?> base = elements.get("");
        if(base != null){
            if(base instanceof TextInputField.Holder<T,?> holder){
                return (Map<String, Object>) holder.inputParser(object).encodeObjectUnchecked(object);
            }
            return (Map<String, Object>) base.inputParser().encodeObjectUnchecked(object);
        }

        Map<String, Object> map = new HashMap<>();

        encodeMap.apply(object).forEach((key, value) ->{
            map.put(
                keyField.inputParser().encodeObjectUnchecked(key).toString(),
                valueField.inputParser().encodeObjectUnchecked(value)
            );
        });

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
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        if(!(object instanceof Map<?,?> map)) throw new IllegalArgumentException(object + " is not Map!");
        Map<Object, Object> parsed = new HashMap<>();
        map.forEach((id, value) ->{
            TextInputField<?, ?> field = elements.get(id.toString());
            Object decoded;
            if(field == null) decoded = valueField.inputParser().decodeObject(value);
            else decoded = field.inputParser().decodeObject(value);

            Object parsedKey = keyField.inputParser().decodeObject(id);
            parsed.put(parsedKey, decoded);
        });
        InputDecodeContext ctx = InputDecodeContext.context(parsed);
        return resultParser.parse(ctx);
    }

    public static class Builder<T> implements PersistTextParser.DynamicMapBuilder<T> {
        protected final Map<String, TextInputField<T, ?>> elements = new HashMap<>();
        protected TextInputResultParser<T> resultParser;
        protected PersistentDataType<PersistentDataContainer, T> dataType;
        protected Class<T> dataTypeClass;
        protected Function<PersistTextParser<T>, PersistentDataType<PersistentDataContainer, T>> function;
        protected TextInputField<T, ?> keyField;
        protected TextInputField<T, ?> valueField;
        protected Function<T, Map<Object, Object>> mapToEncode;

        @Override
        public DynamicMapBuilder<T> field(String name, TextInputField<T, ?> field) {
            elements.put(name, field);
            return this;
        }

        @Override
        public DynamicMapBuilder<T> resultParser(TextInputResultParser<T> resultParser) {
            this.resultParser = resultParser;
            return this;
        }

        @Override
        public PersistTextParser<T> apply(TextInputResultParser<T> resultParser) {
            return resultParser(resultParser).build();
        }

        @Override
        public DynamicMapBuilder<T> dataType(PersistentDataType<PersistentDataContainer, T> dataType) {
            this.dataType = dataType;
            return this;
        }

        @Override
        public DynamicMapBuilder<T> dataTypeClass(Class<T> type) {
            return this;
        }

        @Override
        public PersistTextParser<T> build() {
            if(function != null){
                return new DynamicMapPersistTextParser<>(elements, resultParser, function, keyField, valueField, mapToEncode);
            }
            if(dataType == null) return new DynamicMapPersistTextParser<>(elements, resultParser, dataTypeClass, keyField, valueField, mapToEncode);
            return new DynamicMapPersistTextParser<>(elements, resultParser, dataType, keyField, valueField, mapToEncode);
        }

        @Override
        public PersistTextParser<T> buildUnset() {
            if(dataType == null) return new UnsetMapPersistTextParser<>(elements, resultParser, dataTypeClass);
            return new UnsetMapPersistTextParser<>(elements, resultParser, dataType);
        }

        @Override
        public DynamicMapBuilder<T> dataTypeFunction(Function<PersistTextParser<T>, PersistentDataType<PersistentDataContainer, T>> function) {
            this.function = function;
            return this;
        }

        @Override
        public DynamicMapBuilder<T> keyField(TextInputField<T, ?> field) {
            this.keyField = field;
            return this;
        }

        @Override
        public DynamicMapBuilder<T> valueField(TextInputField<T, ?> field) {
            this.valueField = field;
            return this;
        }

        @Override
        public DynamicMapBuilder<T> mapToEncode(Function<T, Map<Object, Object>> function) {
            this.mapToEncode = function;
            return this;
        }
    }
}
