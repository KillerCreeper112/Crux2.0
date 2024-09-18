package killercreepr.crux.item.dynamic.components.persistence;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.persistence.impl.ListTagType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface DynamicPersistentTag<E, S> {
    DynamicPersistentTag<Object, String> STRING = new SimpleDynamicPersistentTag<>(PersistentDataType.STRING) {
        @Override
        public @Nullable <T extends PersistentDataContainer> String parse(@NotNull T to, @NotNull TextParserContext ctx, @NotNull Object value) {
            return ctx.deserializeString(value.toString());
        }
    };

    DynamicPersistentTag<List<TypedDynamicPersistentTag>, PersistentDataContainer> TAG_CONTAINER = new SimpleDynamicPersistentTag<>(PersistentDataType.TAG_CONTAINER) {
        @Override
        public @Nullable <T extends PersistentDataContainer> PersistentDataContainer parse(@NotNull T to, @NotNull TextParserContext ctx, @NotNull List<TypedDynamicPersistentTag> value) {
            PersistentDataContainer c = to.getAdapterContext().newPersistentDataContainer();
            value.forEach(typed -> typed.apply(c, ctx));
            return c;
        }
    };

    static <T> DynamicPersistentTag<List<?>, List<?>> list(@NotNull DynamicPersistentTag subType){
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
}
