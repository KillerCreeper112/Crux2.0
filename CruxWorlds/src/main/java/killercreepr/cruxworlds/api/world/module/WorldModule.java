package killercreepr.cruxworlds.api.world.module;

import org.bukkit.Chunk;

public interface WorldModule {
    default void onInitiate(){}
    default void onLoad(){}
    default void onUnload(boolean save){}
    default void onSave(){}
    default void onDelete(){}
    default void onPreDelete(){}
    default void onChunkLoad(Chunk chunk){}
    default void onChunkUnload(Chunk chunk){}
    default void onChunkPopulate(Chunk chunk){}
    default void onCreate(){}
}
