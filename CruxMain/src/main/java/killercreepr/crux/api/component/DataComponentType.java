package killercreepr.crux.api.component;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.parser.hybrid.PersistParser;
import killercreepr.crux.api.component.serialization.ComponentSerializer;
import org.jetbrains.annotations.Nullable;

public interface DataComponentType<T> {
    static <T> Builder<T> builder(){
        return new Builder.SimpleBuilder<>();
    }

    @Nullable
    ComponentSerializer<?, T> serializer();

    @Nullable
    ComponentTextInputParser<T> textParser();

    interface Notify<T> extends DataComponentType<T>{
        void onComponentApplied(DataComponentAccessor holder, T value, T previousValue);
        void onComponentRemoved(DataComponentAccessor holder, T previousValue);
    }

    interface Builder<T>{
        DataComponentType<T> build();
        Builder<T> persistent(@Nullable ComponentSerializer<?, T> serializer);
        Builder<T> textParser(@Nullable ComponentTextInputParser<T> parser);
        Builder<T> onRemove(@Nullable NotifyReceiverRemove<T> parser);
        Builder<T> onApply(@Nullable NotifyReceiverApply<T> parser);
        default Builder<T> persistTextParser(@Nullable PersistParser<T> parser){
            return persistent(parser).textParser(parser);
        }

        default Builder<T> textParserUnchecked(@Nullable ComponentTextInputParser<?> parser){
            return textParser((ComponentTextInputParser<T>) parser);
        }

        class SimpleBuilder<T> implements Builder<T>{

            protected @Nullable ComponentSerializer<?, T> serializer;
            protected @Nullable ComponentTextInputParser<T> textParser;
            protected @Nullable NotifyReceiverApply<T> notifyReceiverApply;
            protected @Nullable NotifyReceiverRemove<T> notifyReceiverRemove;
            @Override
            public DataComponentType<T> build() {
                if(notifyReceiverApply != null || notifyReceiverRemove != null){
                    return new SimpleNotify<>(serializer, textParser, notifyReceiverApply, notifyReceiverRemove);
                }
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

            @Override
            public Builder<T> onRemove(@Nullable NotifyReceiverRemove<T> parser) {
                notifyReceiverRemove = parser;
                return this;
            }

            @Override
            public Builder<T> onApply(@Nullable NotifyReceiverApply<T> parser) {
                notifyReceiverApply = parser;
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

    class SimpleNotify<T> extends Simple<T> implements Notify<T>{
        protected final @Nullable NotifyReceiverApply<T> notifyReceiverApply;
        protected final @Nullable NotifyReceiverRemove<T> notifyReceiverRemove;

        public SimpleNotify(@Nullable ComponentSerializer<?, T> serializer, @Nullable ComponentTextInputParser<T> textParser, @Nullable NotifyReceiverApply<T> notifyReceiverApply, @Nullable NotifyReceiverRemove<T> notifyReceiverRemove) {
            super(serializer, textParser);
            this.notifyReceiverApply = notifyReceiverApply;
            this.notifyReceiverRemove = notifyReceiverRemove;
        }

        @Override
        public void onComponentApplied(DataComponentAccessor holder, T value, T previousValue) {
            if(notifyReceiverApply != null) notifyReceiverApply.onComponentApplied(holder, value, previousValue);
        }

        @Override
        public void onComponentRemoved(DataComponentAccessor holder, T previousValue) {
            if(notifyReceiverRemove != null) notifyReceiverRemove.onComponentRemoved(holder, previousValue);
        }
    }

    interface NotifyReceiverApply<T>{
        void onComponentApplied(DataComponentAccessor holder, T value, T previousValue);
    }
    interface NotifyReceiverRemove<T>{
        void onComponentRemoved(DataComponentAccessor holder, T previousValue);
    }
}
