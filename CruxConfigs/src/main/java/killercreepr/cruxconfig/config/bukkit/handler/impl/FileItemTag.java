package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.Crux;
import killercreepr.crux.data.tag.item.ItemTag;
import killercreepr.crux.data.tag.item.ItemTypeTag;
import killercreepr.crux.data.tag.item.SimpleItemTypeTag;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;

public class FileItemTag extends SimpleFileHandler<ItemTag> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemTag object) {
        throw new UnsupportedOperationException("unsupported");
    }

    private ItemTag reference(@NotNull FileContext<?> ctx, @NotNull FileElement e){
        if(e instanceof FileGeneric single){
            String itemKey = single.getAsString();
            if(itemKey.startsWith("#")){
                //minecraft:blocks/planks
                Key tagKey = Crux.key(itemKey.substring(1));
                if(tagKey.namespace().equalsIgnoreCase("minecraft") && tagKey.value().contains("/")){
                    String[] parts = tagKey.value().split("/", 2);
                    if(parts.length > 1){
                        Tag<Material> tag = Bukkit.getTag(parts[0], NamespacedKey.minecraft(parts[1]), Material.class);
                        if(tag == null) throw new NullPointerException("Vanilla tag of " + parts[0] + "/" + parts[1] + " does not exist!");
                        return ItemTag.itemTag(tag);
                    }
                }
                return CruxRegistries.ITEM_TAG.get(Crux.key(itemKey.substring(1)));
            }
        }
        return null;
    }

    @Override
    public @Nullable ItemTag deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        ItemTag tag = reference(ctx, e);
        if(tag != null) return tag;
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return deserializeFromFile(ctx, e, key);
    }

    public @Nullable ItemTag deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key){
        if(!(e instanceof FileObject o)) return null;
        FileElement values = o.get("values");
        if(values==null) return null;
        return deserializeValues(ctx, values, key);
    }
    public @Nullable ItemTag deserializeValues(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
        if(e instanceof FileGeneric single){
            String itemKey = single.getAsString();
            if(itemKey.startsWith("#")){
                return reference(ctx, e);
            }
            return ItemTag.itemTag(key, Crux.key(itemKey));
        }
        if(!(e instanceof FileArray a)) return null;
        Collection<Key> values = new HashSet<>();
        a.forEach(ele ->{
            String itemKey = ele.getAsString();
            if(itemKey.startsWith("#")){
                ItemTag itemTag = CruxRegistries.ITEM_TAG.get(Crux.key(itemKey.substring(1)));
                if(itemTag instanceof ItemTypeTag tag) values.addAll(tag.getTypes());
                else Crux.log(Level.WARNING, "Could not find tag, " + itemKey + "!");
                return;
            }
            values.add(Crux.key(ele.getAsString()));
        });
        if(values.isEmpty()) return null;
        return new SimpleItemTypeTag(key, values);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_tag";
    }
}
