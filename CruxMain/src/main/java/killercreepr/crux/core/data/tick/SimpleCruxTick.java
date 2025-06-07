package killercreepr.crux.core.data.tick;

import killercreepr.crux.api.data.CruxTick;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleCruxTick implements CruxTick {
    protected final Key key;
    protected boolean remove = false;

    public SimpleCruxTick(@NotNull Key key) {
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
