package killercreepr.crux.api.codec.builder;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.builder.field.Field;
import killercreepr.crux.core.codec.ObjectCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class CodecBuilder<T> {
    public static <T> CodecBuilder<T> builder(Supplier<T> constructor){
        return new CodecBuilder<>(constructor);
    }
    public static <T> CodecBuilder<T> builder(Class<T> type, Supplier<T> constructor){
        return new CodecBuilder<>(constructor);
    }

    private final Supplier<T> constructor;
    private final List<Field<T, ?>> fields = new ArrayList<>();

    private CodecBuilder(Supplier<T> constructor) {
        this.constructor = constructor;
    }

    public static <T> CodecBuilder<T> of(Supplier<T> constructor) {
        return new CodecBuilder<>(constructor);
    }

    public <V> CodecBuilder<T> field(
            String name,
            Codec<V> codec,
            Function<T, V> getter,
            BiConsumer<T, V> setter
    ) {
        fields.add(new Field<>(name, codec, getter, setter, false, null));
        return this;
    }

    public <V> CodecBuilder<T> optionalField(
      String name,
      Codec<V> codec,
      V defaultValue,
      Function<T, V> getter,
      BiConsumer<T, V> setter
    ) {
        fields.add(new Field<>(name, codec, getter, setter, true, defaultValue));
        return this;
    }

    public Codec<T> build() {
        return new ObjectCodec<>(constructor, fields);
    }
}