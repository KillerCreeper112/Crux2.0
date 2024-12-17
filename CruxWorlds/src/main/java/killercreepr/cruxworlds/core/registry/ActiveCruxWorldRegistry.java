package killercreepr.cruxworlds.core.registry;

import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxworlds.api.world.CruxWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActiveCruxWorldRegistry extends SimpleMappedRegistry<UUID, CruxWorld> {
    public ActiveCruxWorldRegistry(@NotNull Map<UUID, CruxWorld> map) {
        super(map);
    }

    public ActiveCruxWorldRegistry() {
    }

    public CruxWorld getByName(@NotNull String name){
        return BY_NAME.get(name);
    }

    protected final Map<String, CruxWorld> BY_NAME = new HashMap<>();
    @Override
    public <E extends CruxWorld> @NotNull E register(@NotNull UUID key, @NotNull E value) {
        BY_NAME.put(value.getName(), value);
        return super.register(key, value);
    }

    @Override
    public <E extends CruxWorld> @NotNull E register(@NotNull E object) {
        return register(object.getUUID(), object);
    }

    @Override
    public boolean unregister(@NotNull CruxWorld object) {
        return remove(object.getUUID()) != null;
    }

    @Override
    public @Nullable CruxWorld remove(@NotNull UUID key) {
        CruxWorld removed = super.remove(key);
        if(removed != null) BY_NAME.remove(removed.getName());
        return removed;
    }

    @Override
    public boolean remove(@NotNull UUID key, @NotNull CruxWorld value) {
        boolean x = super.remove(key, value);
        BY_NAME.remove(value.getName());
        return x;
    }
}
