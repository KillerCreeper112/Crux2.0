package killercreepr.cruxstructures.api.component;

import killercreepr.cruxstructures.api.world.module.StructureWorldModule;

public interface TickedStoredComponent {
    void storedTick(StructureWorldModule module);
    default void storedStarted(StructureWorldModule module){}
    default void storedStopped(StructureWorldModule module){}
    //default boolean storedShouldStop(){ return false; }
}
