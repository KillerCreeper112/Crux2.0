package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.key.tag.KeyTag;
import killercreepr.crux.api.key.tag.KeyTypeTag;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;

public class FileKeyTag extends SimpleFileHandler<KeyTag> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull KeyTag object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable KeyTag deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        KeyTag tag = reference(ctx, e);
        if(tag != null) return tag;
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return deserializeFromFile(ctx, e, key);
    }

    private KeyTag reference(@NotNull FileContext<?> ctx, @NotNull FileElement e){
        if(e instanceof FileGeneric single){
            String itemKey = single.getAsString();
            if(itemKey.startsWith("#")){
                return CruxRegistries.KEY_TAG.get(Crux.key(itemKey.substring(1)));
            }
        }
        return null;
    }

    public @Nullable KeyTag deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key){
        if(!(e instanceof FileObject o)) return null;
        FileElement values = o.get("values");
        if(values==null) return null;
        return deserializeValues(ctx, values, key);
    }
    public @Nullable KeyTag deserializeValues(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
        if(e instanceof FileGeneric single){
            String itemKey = single.getAsString();
            if(itemKey.startsWith("#")){
                return CruxRegistries.KEY_TAG.get(Crux.key(itemKey.substring(1)));
            }
            return KeyTag.keyTag(key, Crux.key(itemKey));
        }
        if(!(e instanceof FileArray a)) return null;
        Collection<Key> values = new HashSet<>();
        a.forEach(ele ->{
            String itemKey = ele.getAsString();
            if(itemKey.startsWith("#")){
                KeyTag itemTag = CruxRegistries.KEY_TAG.get(Crux.key(itemKey.substring(1)));
                if(itemTag instanceof KeyTypeTag tag) values.addAll(tag.getTypes());
                else Crux.log(Level.WARNING, "Could not find tag, " + itemKey + "!");
                return;
            }
            values.add(Crux.key(ele.getAsString()));
        });
        if(values.isEmpty()) return null;
        return KeyTag.keyTag(key, values);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "key_tag";
    }
}
