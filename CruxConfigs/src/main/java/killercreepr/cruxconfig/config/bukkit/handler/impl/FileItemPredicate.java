package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.cruxconfig.config.common.handler.FromCodecFileHandler;
import org.jetbrains.annotations.NotNull;

public class FileItemPredicate extends FromCodecFileHandler<ItemPredicate> {
    public FileItemPredicate(Codec<ItemPredicate> codec) {
        super(codec);
    }

    /*@Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemPredicate object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable ItemPredicate deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        if(e instanceof FileGeneric g){
            String key = g.getAsString();
            if(key.startsWith("#")){
                ItemTag tag = registry.deserializeFromFile(ItemTag.class, g, ctx);
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
    }*/
    @Override
    public @NotNull String jsonSerializerID() {
        return "item_predicate";
    }
}
