package killercreepr.cruxconfig.config.bukkit.handler.impl;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.data.tag.block.BlockTag;
import killercreepr.crux.Crux;
import killercreepr.crux.block.predicate.BlockAllPredicate;
import killercreepr.crux.block.predicate.BlockAnyPredicate;
import killercreepr.crux.block.predicate.BlockPredicate;
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

public class FileBlockPredicate extends SimpleFileHandler<BlockPredicate> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull BlockPredicate object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable BlockPredicate deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        if(e instanceof FileGeneric g){
            String key = g.getAsString();
            if(key.startsWith("#")){
                BlockTag tag = CruxRegistries.BLOCK_TAG.get(Crux.key(key.substring(1)));
                if(tag==null) return null;
                //todo return BlockPredicate.fromTag(tag);
            }
            return BlockPredicate.fromType(Crux.key(key));
        }
        if(e instanceof FileArray a){
            Collection<BlockPredicate> children = new ArrayList<>();
            a.forEach(ele ->{
                BlockPredicate predicate = registry.deserializeFromFile(BlockPredicate.class, ele, ctx);
                if(predicate==null) return;
                children.add(predicate);
            });
            return BlockPredicate.fromAllOf(children);
        }
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type == null) return null;
        return switch (type.toLowerCase()){
            case "any_of" ->{
                Collection<BlockPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<BlockPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield new BlockAnyPredicate(values);
            }
            case "all_of" ->{
                Collection<BlockPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<BlockPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield new BlockAllPredicate(values);
            }
            default ->{
                Crux.log(Level.WARNING, "No item predicate of " + type + " exists!");
                yield null;
            }
        };
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "block_predicate";
    }
}
