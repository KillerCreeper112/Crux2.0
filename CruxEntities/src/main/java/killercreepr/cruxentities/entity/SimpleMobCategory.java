package killercreepr.cruxentities.entity;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleMobCategory implements MobCategory{
    protected final @NotNull Key key;

    public SimpleMobCategory(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(!(obj instanceof MobCategory c)) return false;
        return c.key().equals(key);
    }
}
