package killercreepr.crux.core.item.tag;

import killercreepr.crux.api.item.tag.ItemTypeTag;
import killercreepr.crux.core.Crux;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class SimpleItemTypeTag extends BaseItemTag implements ItemTypeTag {
    public static SimpleItemTypeTag.Builder builder(){
        return new Builder();
    }

    public static SimpleItemTypeTag.Builder builder(@NotNull Key key){
        return new Builder(key);
    }

    protected final @NotNull Collection<Key> values;
    public SimpleItemTypeTag(@NotNull Key key, @NotNull Collection<Key> values) {
        super(key);
        this.values = values;
    }

    @Override
    public boolean isTagged(@NotNull ItemStack item) {
        return values.contains(Crux.handlers().item().getType(item));
    }

    public @NotNull Collection<ItemStack> getValues() {
        Collection<ItemStack> list = new HashSet<>();
        values.forEach(m ->{
            ItemHolder holder = Crux.handlers().item().getItem(m);
            if(holder==null) return;
            list.add(holder.value());
        });
        return list;
    }

    @Override
    public @NotNull Collection<Key> getTypes(){
        return values;
    }

    public static class Builder {
        protected @NotNull Collection<Key> values = new HashSet<>();
        protected @NotNull Key key;

        public Builder() {
        }
        public Builder(@NotNull Key key){
            this.key = key;
        }

        public Builder add(Material... m){
            for(Material mm : m){
                add(mm.key());
            }
            return this;
        }

        public Builder add(Key m){
            values.add(m);
            return this;
        }

        public Builder add(Key... m){
            values.addAll(Arrays.asList(m));
            return this;
        }

        public Builder values(Collection<Key> values) {
            this.values = values;
            return this;
        }

        public Builder key(Key key) {
            this.key = key;
            return this;
        }

        public SimpleItemTypeTag build() {
            return new SimpleItemTypeTag(key, values);
        }
    }
}
