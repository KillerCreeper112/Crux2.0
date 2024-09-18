package killercreepr.crux.item.dynamic.components.persistence;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxTag;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleDynamicPersistentTag<E, S> implements DynamicPersistentTag<E, S>{
    protected final @NotNull PersistentDataType<?, S> dataType;
    public SimpleDynamicPersistentTag(@NotNull PersistentDataType<?, S> dataType) {
        this.dataType = dataType;
    }

    @Override
    public <T extends PersistentDataContainer> T apply(@NotNull T to, @NotNull TextParserContext ctx, @NotNull String tagKey, @NotNull E value) {
        S parsed = parse(to, ctx, value);
        if(parsed == null){
            return CruxTag.remove(to, tagKey);
        }
        return CruxTag.set(to, tagKey, dataType, parsed);
    }

    @Override
    public @NotNull PersistentDataType<?, S> getDataType() {
        return dataType;
    }
}
