package killercreepr.crux.item.dynamic.components.persistence;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.persistence.impl.ListTagType;
import killercreepr.crux.util.CruxMath;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface DynamicPersistentTag<E, S> {
    DynamicPersistentTag<Object, String> STRING = new SimpleDynamicPersistentTag<>(PersistentDataType.STRING) {
        @Override
        public @Nullable <T extends PersistentDataContainer> String parse(@NotNull T to, @NotNull TextParserContext ctx, @NotNull Object value) {
            return ctx.deserializeString(value.toString());
        }
    };
    DynamicPersistentTag<Object, Double> DOUBLE = number(PersistentDataType.DOUBLE, Number::doubleValue);
    DynamicPersistentTag<Object, Float> FLOAT = number(PersistentDataType.FLOAT, Number::floatValue);
    DynamicPersistentTag<Object, Integer> INTEGER = number(PersistentDataType.INTEGER, Number::intValue);
    DynamicPersistentTag<Object, Short> SHORT = number(PersistentDataType.SHORT, Number::shortValue);
    DynamicPersistentTag<Object, Byte> BYTE = number(PersistentDataType.BYTE, Number::byteValue);
    DynamicPersistentTag<Object, Long> LONG = number(PersistentDataType.LONG, Number::longValue);

    DynamicPersistentTag<List<TypedDynamicPersistentTag<?>>, PersistentDataContainer> TAG_CONTAINER = new SimpleDynamicPersistentTag<>(PersistentDataType.TAG_CONTAINER) {
        @Override
        public @Nullable <T extends PersistentDataContainer> PersistentDataContainer parse(@NotNull T to, @NotNull TextParserContext ctx, @NotNull List<TypedDynamicPersistentTag<?>> value) {
            PersistentDataContainer c = to.getAdapterContext().newPersistentDataContainer();
            value.forEach(typed -> typed.apply(c, ctx));
            return c;
        }
    };

    static DynamicPersistentTag<List<?>, List<?>> list(@NotNull DynamicPersistentTag subType){
        return new SimpleDynamicPersistentTag<List<?>, List<?>>(new ListTagType<>(subType.getDataType())) {

            @Override
            public @Nullable <T extends PersistentDataContainer> List<?> parse(@NotNull T to, @NotNull TextParserContext ctx, @NotNull List<?> value) {
                List<Object> list = new ArrayList<>();
                value.forEach(v ->{
                    Object parsed = subType.parseUnchecked(to, ctx, v);
                    if(parsed==null) return;
                    list.add(parsed);
                });
                return list;
            }
        };
    }

    @NotNull PersistentDataType<?, S> getDataType();
    <T extends PersistentDataContainer> T apply(@NotNull T to, @NotNull TextParserContext ctx, @NotNull String tagKey, @NotNull E value);
    default <T extends PersistentDataContainer> @Nullable S parseUnchecked(@NotNull T to, @NotNull TextParserContext ctx, @NotNull Object value){
        return parse(to, ctx, (E) value);
    }
    <T extends PersistentDataContainer> @Nullable S parse(@NotNull T to, @NotNull TextParserContext ctx, @NotNull E value);

    private static <T extends Number> DynamicPersistentTag<Object, T> number(
        PersistentDataType<?, T> dataType, Function<Number, T> convert
    ){
        return new SimpleDynamicPersistentTag<>(dataType) {
            @Override
            public <T1 extends PersistentDataContainer> @Nullable T parse(@NotNull T1 to, @NotNull TextParserContext ctx, @NotNull Object value) {
                Number n = CruxMath.evaluate(ctx.deserializeString(value.toString()));
                return convert.apply(n);
            }
        };
    }
}
