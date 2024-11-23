package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ElementPersistTextParser<T> implements PersistTextParser<T> {
    protected final @NotNull TextInputField<T, ?> field;
    protected final @NotNull TextInputResultParser<T> resultParser;
    protected final @NotNull PersistentDataType<?, T> dataType;

    public ElementPersistTextParser(@NotNull TextInputField<T, ?> field,
                                    @NotNull TextInputResultParser<T> resultParser,
                                    @NotNull PersistentDataType<?, T> dataType) {
        this.field = field;
        this.resultParser = resultParser;
        this.dataType = dataType;
    }

    public ElementPersistTextParser(@NotNull TextInputField<T, ?> field,
                                    @NotNull TextInputResultParser<T> resultParser,
                                    @NotNull Class<T> type) {
        this.field = field;
        this.resultParser = resultParser;
        this.dataType = buildDataType(type, field);
    }

    public PersistentDataType<?, T> buildDataType(@NotNull Class<T> complexType,
                                                  @NotNull TextInputField<T, ?> element){
        return new PersistentDataType<>() {
            @Override
            public @NotNull Class<Object> getPrimitiveType() {
                return (Class<Object>) element.inputParser().dataType().getPrimitiveType();
            }

            @Override
            public @NotNull Class<T> getComplexType() {
                return complexType;
            }

            @Override
            public @NotNull Object toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
                Object object;
                try{
                    T test =(T) complex;
                    object = element.parseField(complex);
                }catch (ClassCastException ignored){
                    object = complex;
                }


                PersistentDataType type = element.inputParser().dataType();
                return type.toPrimitive(object, context);
            }

            @Override
            public @NotNull T fromPrimitive(@NotNull Object primitive, @NotNull PersistentDataAdapterContext context) {
                PersistentDataType type = element.inputParser().dataType();
                Object parsed = type.fromPrimitive(primitive, context);
                InputDecodeContext ctx = InputDecodeContext.context(parsed);
                return resultParser.parse(ctx);
            }

        };
    }

    @Override
    public @NotNull PersistentDataType<?, T> dataType() {
        return dataType;
    }

    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        Object parsed = field.inputParser().decodeObject(object);
        InputDecodeContext ctx = InputDecodeContext.context(parsed);
        return resultParser.parse(ctx);
    }

    @Override
    public @NotNull Object encodeObject(@NotNull T object) {
        Object parsed = field.parseField(object);
        return field.inputParser().encodeObjectUnchecked(parsed);
    }

    public static class Builder<T> implements PersistTextParser.ElementBuilder<T> {
        protected TextInputField<T, ?> field;
        protected TextInputResultParser<T> resultParser;
        protected PersistentDataType<PersistentDataContainer, T> dataType;
        protected Class<T> dataTypeClass;

        @Override
        public Builder<T> field(TextInputField<T, ?> field) {
            this.field = field;
            return this;
        }

        @Override
        public Builder<T> resultParser(TextInputResultParser<T> resultParser) {
            this.resultParser = resultParser;
            return this;
        }

        @Override
        public PersistTextParser<T> apply(TextInputResultParser<T> resultParser) {
            return resultParser(resultParser).build();
        }

        @Override
        public Builder<T> dataType(PersistentDataType<PersistentDataContainer, T> dataType) {
            this.dataType = dataType;
            return this;
        }

        @Override
        public Builder<T> dataTypeClass(Class<T> type) {
            return this;
        }

        @Override
        public PersistTextParser<T> build() {
            if(dataType == null) return new ElementPersistTextParser<>(field, resultParser, dataTypeClass);
            return new ElementPersistTextParser<>(field, resultParser, dataType);
        }
    }
}
