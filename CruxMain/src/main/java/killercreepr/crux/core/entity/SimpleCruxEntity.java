package killercreepr.crux.core.entity;

import killercreepr.crux.api.component.serialization.PersistHolderComponentHandler;
import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.core.persistence.CruxPersist;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleCruxEntity implements CruxEntity, PersistHolderComponentHandler {
    protected final @NotNull Entity entity;
    public SimpleCruxEntity(@NotNull Entity entity) {
        this.entity = entity;
    }

    @Override
    public @Nullable PersistentDataHolder getComponentsPersistentHolder() {
        return entity;
    }

    @Override
    public void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data) {
        PersistentDataHolder item = getComponentsPersistentHolder();
        CruxPersist.COMPONENTS.set(item, data.isEmpty() ? null : data);
    }

    @Override
    public void clearComponents() {
        CruxPersist.COMPONENTS.set(entity(), null);
    }

    @Override
    public @NotNull Entity entity() {
        return entity;
    }


}
