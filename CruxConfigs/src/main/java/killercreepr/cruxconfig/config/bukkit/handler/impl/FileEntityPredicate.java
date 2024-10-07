package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.Crux;
import killercreepr.crux.block.predicate.BlockPredicate;
import killercreepr.crux.data.tag.block.BlockTag;
import killercreepr.crux.entity.predicate.EntityPredicate;
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

public class FileEntityPredicate extends SimpleFileHandler<EntityPredicate> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull EntityPredicate object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable EntityPredicate deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        if(e instanceof FileGeneric g){
            String key = g.getAsString();
            if(key.startsWith("#")){
                BlockTag tag = CruxRegistries.BLOCK_TAG.get(Crux.key(key.substring(1)));
                if(tag==null) return null;
                //todo return EntityPredicate.fromTag(tag);
            }
            return EntityPredicate.fromType(Crux.key(key));
        }
        if(e instanceof FileArray a){
            Collection<BlockPredicate> children = new ArrayList<>();
            a.forEach(ele ->{
                BlockPredicate predicate = registry.deserializeFromFile(BlockPredicate.class, ele, ctx);
                if(predicate==null) return;
                children.add(predicate);
            });
            //todo return EntityPredicate.fromAllOf(children);
        }
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type == null) return null;
        return switch (type.toLowerCase()){
            /*todo case "any_of" ->{
                Collection<BlockPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<BlockPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield new EntityAnyPredicate(values);
            }
            case "all_of" ->{
                Collection<BlockPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<BlockPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield new EntityAllPredicate(values);
            }*/
            default ->{
                Crux.log(Level.WARNING, "No entity predicate of " + type + " exists!");
                yield null;
            }
        };
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "entity_predicate";
    }
}
