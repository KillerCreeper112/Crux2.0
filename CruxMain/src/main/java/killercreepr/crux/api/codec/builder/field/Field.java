package killercreepr.crux.api.codec.builder.field;

import killercreepr.crux.api.codec.Codec;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class Field<T, V> {
    public final String name;
    public final Codec<V> codec;
    public final Function<T, V> getter;
    public final BiConsumer<T, V> setter;
    public final boolean optional;
    public final V defaultValue;

    public Field(String name, Codec<V> codec,
                 Function<T, V> getter,
                 BiConsumer<T, V> setter,
                 boolean optional,
                 V defaultValue) {
        this.name = name;
        this.codec = codec;
        this.getter = getter;
        this.setter = setter;
        this.optional = optional;
        this.defaultValue = defaultValue;
    }
}
