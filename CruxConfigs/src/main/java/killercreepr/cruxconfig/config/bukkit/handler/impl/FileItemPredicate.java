package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.Crux;
import killercreepr.crux.ItemTag;
import killercreepr.crux.item.predicate.ItemPredicate;
import killercreepr.crux.item.predicate.ItemTypePredicate;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class FileItemPredicate extends SimpleFileHandler<ItemPredicate> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemPredicate object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable ItemPredicate deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        if(e instanceof FileGeneric g){
            String key = g.getAsString();
            if(key.startsWith("#")){
                ItemTag tag = CruxRegistries.ITEM_TAG.get(Crux.key(key.substring(1)));
                if(tag==null) return null;
                return ItemPredicate.fromTag(tag);
            }
            return ItemPredicate.fromType(Crux.key(key));
        }
        if(e instanceof FileArray a){
            Collection<ItemPredicate> children = new ArrayList<>();
            a.forEach(ele ->{
                ItemPredicate predicate = registry.deserializeFromFile(ItemTypePredicate.class, ele, ctx);
                if(predicate==null) return;
                children.add(predicate);
            });
            return ItemPredicate.fromAllOf(children);
        }
        return null;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_predicate";
    }
}
