package killercreepr.cruxstructures.api.structure;

import killercreepr.cruxstructures.api.world.module.StructureWorldModule;
import org.jetbrains.annotations.NotNull;

public interface TickedStoredStructure extends StoredStructure {
    default void started(@NotNull StructureWorldModule module){}
    default void stopped(@NotNull StructureWorldModule module){}
    default boolean shouldStop(@NotNull StructureWorldModule module){ return false; }
    void tick(@NotNull StructureWorldModule module);
}
