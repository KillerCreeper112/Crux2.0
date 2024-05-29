package killercreepr.crux.data.tick;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public abstract class CruxTick implements Keyed {
    protected final NamespacedKey key;
    protected boolean remove = false;

    public CruxTick(@NotNull NamespacedKey key) {
        this.key = key;
    }

    public abstract void tick();

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    public boolean markedForRemoval(){ return remove; }

    public CruxTick markRemoval(boolean remove){
        this.remove = remove;
        return this;
    }
}
