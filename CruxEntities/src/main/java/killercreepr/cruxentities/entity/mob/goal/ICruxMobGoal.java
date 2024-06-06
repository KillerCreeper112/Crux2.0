package killercreepr.cruxentities.entity.mob.goal;

public interface ICruxMobGoal {
    boolean shouldActivate();
    default boolean shouldStayActive() {
        return shouldActivate();
    }
    default void start() {}
    default void stop() {}
    default void tick() {}
    boolean isInAttackRange(double distance);
}
