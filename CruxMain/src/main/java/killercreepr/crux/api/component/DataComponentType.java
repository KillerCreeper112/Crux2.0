package killercreepr.crux.api.component;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.parser.persistent.PersistentComponentInputParser;
import killercreepr.crux.api.component.serialization.ComponentSerializer;
import killercreepr.crux.core.registries.CruxRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataComponentType<T> {
    static <T> Builder<T> builder(){
        return new Builder.SimpleBuilder<>();
    }

    @Nullable
    ComponentSerializer<?, T> serializer();

    @Nullable
    ComponentTextInputParser<T> textParser();

    interface Builder<T>{
        DataComponentType<T> build();
        Builder<T> persistent(@Nullable ComponentSerializer<?, T> serializer);
        Builder<T> textParser(@Nullable ComponentTextInputParser<T> parser);

        default Builder<T> inputParser(@Nullable PersistentComponentInputParser<T> parser){
            return persistent(parser).textParser(parser);
        }

        default Builder<T> textParser(@NotNull Class<T> type){
            return textParserUnchecked(CruxRegistries.DATA_COMPONENT_TEXT_PARSER_TYPE.get(type));
        }
        default Builder<T> textParserUnchecked(@Nullable ComponentTextInputParser<?> parser){
            return textParser((ComponentTextInputParser<T>) parser);
        }

        class SimpleBuilder<T> implements Builder<T>{

            protected @Nullable ComponentSerializer<?, T> serializer;
            protected @Nullable ComponentTextInputParser<T> textParser;
            @Override
            public DataComponentType<T> build() {
                return new Simple<>(serializer, textParser);
            }

            @Override
            public Builder<T> persistent(@Nullable ComponentSerializer<?, T> serializer) {
                this.serializer = serializer;
                return this;
            }

            @Override
            public Builder<T> textParser(@Nullable ComponentTextInputParser<T> parser) {
                this.textParser = parser;
                return this;
            }
        }
    }

    class Simple<T> implements DataComponentType<T>{
        protected final @Nullable ComponentSerializer<?, T> serializer;
        protected final @Nullable ComponentTextInputParser<T> textParser;

        public Simple(@Nullable ComponentSerializer<?, T> serializer, @Nullable ComponentTextInputParser<T> textParser) {
            this.serializer = serializer;
            this.textParser = textParser;
        }

        @Override
        public @Nullable ComponentSerializer<?, T> serializer() {
            return serializer;
        }

        @Override
        public @Nullable ComponentTextInputParser<T> textParser() {
            return textParser;
        }
    }
}
