package killercreepr.crux.api.data.tick;

public interface ManagedTickedTime extends TickedTime {
    default void started(int tick, int tickRate){}
    default void stopped(int tick, int tickRate){}
    default boolean shouldStop(int tick, int tickRate){ return false; }
}
