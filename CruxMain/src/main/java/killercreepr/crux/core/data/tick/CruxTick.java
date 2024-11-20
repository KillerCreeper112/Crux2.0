package killercreepr.crux.core.data.tick;

import killercreepr.crux.api.data.tick.Ticked;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public abstract class CruxTick implements Keyed, Ticked {
    protected final Key key;
    protected boolean remove = false;

    public CruxTick(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public boolean markedForRemoval(){ return remove; }

    public CruxTick markRemoval(boolean remove){
        this.remove = remove;
        return this;
    }
}
