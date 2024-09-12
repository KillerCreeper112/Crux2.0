package killercreepr.cruxconfig.config.bukkit.handler.impl;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.Crux;
import killercreepr.crux.data.tag.item.ItemTag;
import killercreepr.crux.item.predicate.ItemAllPredicate;
import killercreepr.crux.item.predicate.ItemAnyPredicate;
import killercreepr.crux.item.predicate.ItemPredicate;
import killercreepr.crux.item.predicate.ItemTypePredicate;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

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
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type == null) return null;
        return switch (type.toLowerCase()){
            case "any_of" ->{
                Collection<ItemPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<ItemPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield new ItemAnyPredicate(values);
            }
            case "all_of" ->{
                Collection<ItemPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<ItemPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield new ItemAllPredicate(values);
            }
            default ->{
                Crux.log(Level.WARNING, "No item predicate of " + type + " exists!");
                yield null;
            }
        };
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_predicate";
    }
}
