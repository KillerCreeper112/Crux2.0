package killercreepr.crux.component;

import killercreepr.crux.component.serialzation.ComponentSerializer;
import org.jetbrains.annotations.Nullable;

public interface DataComponentType<T> {
    static <T> Builder<T> builder(){
        return new Builder.SimpleBuilder<>();
    }

    @Nullable
    ComponentSerializer<?, T> serializer();

    interface Builder<T>{
        DataComponentType<T> build();
        Builder<T> persistent(@Nullable ComponentSerializer<?, T> serializer);

        class SimpleBuilder<T> implements Builder<T>{

            protected @Nullable ComponentSerializer<?, T> serializer;
            @Override
            public DataComponentType<T> build() {
                return new Simple<>(serializer);
            }

            @Override
            public Builder<T> persistent(@Nullable ComponentSerializer<?, T> serializer) {
                this.serializer = serializer;
                return this;
            }
        }
    }

    class Simple<T> implements DataComponentType<T>{
        protected final @Nullable ComponentSerializer<?, T> serializer;

        public Simple(@Nullable ComponentSerializer<?, T> serializer) {
            this.serializer = serializer;
        }

        @Override
        public @Nullable ComponentSerializer<?, T> serializer() {
            return serializer;
        }
    }
}
