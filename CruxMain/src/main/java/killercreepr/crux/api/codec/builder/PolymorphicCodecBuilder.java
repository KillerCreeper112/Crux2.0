package killercreepr.crux.api.codec.builder;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.core.codec.PolymorphicCodec;

import java.util.HashMap;
import java.util.Map;

public final class PolymorphicCodecBuilder<T> {
    public static <T> PolymorphicCodecBuilder<T> polymorphicBuilder(Class<T> type, String typeField){
        return new PolymorphicCodecBuilder<>(typeField);
    }

    private final String typeField;
    private final Map<String, Codec<? extends T>> codecs = new HashMap<>();

    public PolymorphicCodecBuilder(String typeField) {
        this.typeField = typeField;
    }

    public PolymorphicCodecBuilder<T> register(String type, Codec<? extends T> codec) {
        codecs.put(type, codec);
        return this;
    }

    public PolymorphicCodec<T> build() {
        return new PolymorphicCodec<>(typeField, codecs);
    }
}