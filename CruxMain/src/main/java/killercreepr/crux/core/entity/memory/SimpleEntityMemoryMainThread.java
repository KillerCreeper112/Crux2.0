package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.data.Holder;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SimpleEntityMemoryMainThread extends SimpleEntityMemory{
    public SimpleEntityMemoryMainThread(@NotNull Entity e, DataHolderRegistry dataHolders) {
        super(e, dataHolders);
    }

    public SimpleEntityMemoryMainThread(DataHolderRegistry dataHolders, @NotNull UUID uuid, @NotNull Holder<? extends Entity> holder) {
        super(dataHolders, uuid, holder);
    }

    public SimpleEntityMemoryMainThread(@NotNull Entity e) {
        super(e);
    }

    public SimpleEntityMemoryMainThread(@NotNull UUID uuid, @NotNull Holder<? extends Entity> holder) {
        super(uuid, holder);
    }

    @Override
    public boolean isAsync() {
        return false;
    }
}
