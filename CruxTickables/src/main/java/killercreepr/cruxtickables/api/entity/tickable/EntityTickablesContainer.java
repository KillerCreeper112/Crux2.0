package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.cruxtickables.core.entity.tickable.SimpleEntityTickablesContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface EntityTickablesContainer {
    static EntityTickablesContainer container(Collection<EntityTickableInstance> instances){
        return new SimpleEntityTickablesContainer(instances);
    }

    @NotNull Collection<EntityTickableInstance> getTickableInstances();
}
