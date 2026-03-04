package killercreepr.crux.api.item.tag;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataArray;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.item.tag.BukkitItemTypeTag;
import killercreepr.crux.core.item.tag.SimpleItemTypeTag;
import killercreepr.crux.core.item.tag.SingleItemTypeTag;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;

public interface ItemTag extends Tag<ItemStack> {
    public final Codec<ItemTag> CODEC = new Codec<>() {
        @Override
        public ItemTag decode(DataNode node) throws DecodeException {
            ItemTag tag = reference(node);
            if(tag != null) return tag;
            if(!(node instanceof DataObject o)) return null;
            var keyNode = o.get("key");
            if(keyNode == null) return null;
            Key key = Crux.key(keyNode.asString());
            return deserializeFromFile(node, key);
        }

        @Override
        public DataNode encode(ItemTag value) {
            return null;
        }
    };

    public static @Nullable ItemTag deserializeFromFile(DataNode node, @NotNull Key key){
        if(!(node instanceof DataObject o)) return null;
        var values = o.get("values");
        if(values==null) return null;
        return deserializeValues(values, key);
    }
    public static @Nullable ItemTag deserializeValues(DataNode node, @NotNull Key key) {
        if(node.isString()){
            String itemKey = node.asString();
            if(itemKey.startsWith("#")){
                return reference(node);
            }
            return ItemTag.itemTag(key, Crux.key(itemKey));
        }
        if(!(node instanceof DataArray a)) return null;
        Collection<Key> values = new HashSet<>();
        a.forEachDataNode(ele ->{
            String itemKey = ele.asString();
            if(itemKey.startsWith("#")){
                Key parsedKey = Crux.key(itemKey.substring(1));
                ItemTag itemTag = CruxRegistries.ITEM_TAG.get(parsedKey);
                if(itemTag instanceof ItemTypeTag tag) values.addAll(tag.getTypes());
                else{
                    var tag = Bukkit.getTag(org.bukkit.Tag.REGISTRY_BLOCKS, CruxKey.key(parsedKey), Material.class);
                    if(tag == null){
                        tag = Bukkit.getTag(org.bukkit.Tag.REGISTRY_ITEMS, CruxKey.key(parsedKey), Material.class);
                    }
                    if(tag == null){
                        Crux.log(Level.WARNING, "Could not find tag, " + itemKey + "!");
                    }else{
                        for(Material m : tag.getValues()){
                            values.add(m.key());
                        }
                    }
                }
                return;
            }
            values.add(Crux.key(ele.asString()));
        });
        if(values.isEmpty()) return null;
        return new SimpleItemTypeTag(key, values);
    }

    private static ItemTag reference(DataNode node){
        if(node.isString()){
            String itemKey = node.asString();
            if(itemKey.startsWith("#")){
                //minecraft:blocks/planks
                Key tagKey = Crux.key(itemKey.substring(1));
                if(tagKey.namespace().equalsIgnoreCase("minecraft") && tagKey.value().contains("/")){
                    String[] parts = tagKey.value().split("/", 2);
                    if(parts.length > 1){
                        org.bukkit.Tag<Material> tag = Bukkit.getTag(parts[0], NamespacedKey.minecraft(parts[1]), Material.class);
                        if(tag == null) throw new NullPointerException("Vanilla tag of " + parts[0] + "/" + parts[1] + " does not exist!");
                        return ItemTag.itemTag(tag);
                    }
                }
                var got = CruxRegistries.ITEM_TAG.get(Crux.key(itemKey.substring(1)));
                if(got == null) Crux.logError("No item tag of " + itemKey + " found!");
                return got;
            }
        }
        return null;
    }

    static ItemTag itemTag(@NotNull Key tagType, @NotNull Key type){
        return new SingleItemTypeTag(tagType, type);
    }
    static ItemTag itemTag(org.bukkit.Tag<Material> tag){
        return new BukkitItemTypeTag(tag);
    }
}
