package killercreepr.cruxworlds.world.module;

public interface WorldModule {
    default void onInitiate(){}
    default void onLoad(){}
    default void onUnload(){}
    default void onSave(){}
}
