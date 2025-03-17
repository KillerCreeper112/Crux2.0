package killercreepr.crux.core.entity.tag;

import killercreepr.crux.api.entity.tag.EntityTag;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class SimpleEntityTypeTag extends BaseEntityTag implements EntityTag {
    public static SimpleEntityTypeTag.Builder builder(){
        return new Builder();
    }

    public static SimpleEntityTypeTag.Builder builder(@NotNull Key key){
        return new Builder(key);
    }

    protected final @NotNull Collection<Key> values;
    public SimpleEntityTypeTag(@NotNull Key key, @NotNull Collection<Key> values) {
        super(key);
        this.values = values;
    }

    @Override
    public boolean isTagged(@NotNull Entity item) {
        return values.contains(Crux.handlers().entity().getType(item));
    }

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

        public SimpleEntityTypeTag build() {
            return new SimpleEntityTypeTag(key, values);
        }
    }
}
