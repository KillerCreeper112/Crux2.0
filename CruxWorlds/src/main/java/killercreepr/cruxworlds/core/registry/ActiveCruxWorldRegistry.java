package killercreepr.cruxworlds.core.registry;

import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxworlds.api.world.CruxWorld;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class ActiveCruxWorldRegistry extends SimpleMappedRegistry<Key, CruxWorld> {
    public ActiveCruxWorldRegistry(@NotNull Map<Key, CruxWorld> map) {
        super(map);
    }

    public ActiveCruxWorldRegistry() {
    }

    /*public CruxWorld getByName(@NotNull String name){
        return BY_NAME.get(name);
    }*/

    //protected final Map<String, CruxWorld> BY_NAME = new HashMap<>();
    protected final Collection<ManagedTicked> TICKED = new HashSet<>();

    public Collection<ManagedTicked> getTicked(){
        return TICKED;
    }

    @Override
    public <E extends CruxWorld> @NotNull E register(@NotNull Key key, @NotNull E value) {
        //BY_NAME.put(value.getName(), value);
        if(value instanceof ManagedTicked t){
            TICKED.add(t);
            t.started();
        }
        return super.register(key, value);
    }

    @Override
    public <E extends CruxWorld> @NotNull E register(@NotNull E object) {
        return register(object.key(), object);
    }

    @Override
    public boolean unregister(@NotNull CruxWorld object) {
        return remove(object.key()) != null;
    }

    @Override
    public @Nullable CruxWorld remove(@NotNull Key key) {
        CruxWorld removed = super.remove(key);
        if(removed != null){
            //BY_NAME.remove(removed.getName());
            if(removed instanceof ManagedTicked t){
                if(TICKED.remove(t)) t.stopped();
            }
        }
        return removed;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull CruxWorld value) {
        boolean x = super.remove(key, value);
        //BY_NAME.remove(value.getName());
        if(value instanceof ManagedTicked t){
            if(TICKED.remove(t)) t.stopped();
        }
        return x;
    }
}
