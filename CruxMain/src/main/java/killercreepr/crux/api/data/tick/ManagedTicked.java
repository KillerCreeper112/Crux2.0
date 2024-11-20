package killercreepr.crux.api.data.tick;

public interface ManagedTicked extends Ticked{
    default void started(){}
    default void stopped(){}
    default boolean shouldStop(){ return false; }
}
