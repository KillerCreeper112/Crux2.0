package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.core.component.parser.hybrid.text.persistence.ElementDataListType;
import killercreepr.crux.core.component.parser.hybrid.text.persistence.ElementDataType;
import org.bukkit.persistence.ListPersistentDataType;
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
        if(element.inputParser().dataType() instanceof ListPersistentDataType<?,?>) return new ElementDataListType<>(
            complexType, element, resultParser
        );
        return new ElementDataType<>(complexType, element, resultParser);
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
        protected PersistentDataType<?, T> dataType;
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
        public Builder<T> dataType(PersistentDataType<?, T> dataType) {
            this.dataType = dataType;
            return this;
        }

        @Override
        public Builder<T> dataTypeClass(Class<T> type) {
            this.dataTypeClass = type;
            return this;
        }

        @Override
        public PersistTextParser<T> build() {
            if(dataType == null) return new ElementPersistTextParser<>(field, resultParser, dataTypeClass);
            return new ElementPersistTextParser<>(field, resultParser, dataType);
        }
    }
}
