package killercreepr.crux.data.tick;

public interface ManagedTicked extends Ticked{
    default void started(){}
    default void stopped(){}
    default boolean shouldStop(){ return false; }
}
