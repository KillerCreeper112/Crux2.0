package killercreepr.crux.data.tag.block;

import killercreepr.crux.block.CruxedBlock;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class SimpleBlockTypeTag extends BaseBlockTag implements BlockTypeTag {
    public static SimpleBlockTypeTag.Builder builder(){
        return new Builder();
    }

    public static SimpleBlockTypeTag.Builder builder(@NotNull Key key){
        return new Builder(key);
    }

    protected final @NotNull Collection<Key> values;
    public SimpleBlockTypeTag(@NotNull Key key, @NotNull Collection<Key> values) {
        super(key);
        this.values = values;
    }

    @Override
    public boolean isTagged(@NotNull CruxedBlock item) {
        return values.contains(item.getType());
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

        public SimpleBlockTypeTag build() {
            return new SimpleBlockTypeTag(key, values);
        }
    }
}
