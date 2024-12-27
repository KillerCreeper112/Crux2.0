package killercreepr.cruxstructures.api.component;

public interface TickedStoredComponent {
    void storedTick();
    default void storedStarted(){}
    default void storedStopped(){}
    //default boolean storedShouldStop(){ return false; }
}
