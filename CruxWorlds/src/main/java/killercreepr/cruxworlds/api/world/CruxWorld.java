package killercreepr.cruxworlds.api.world;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.cruxworlds.api.world.module.WorldModule;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public interface CruxWorld extends DataComponentHandler {
    @NotNull
    World toBukkitWorld();
    @NotNull
    Random getRandom();
    @NotNull String getName();
    @NotNull UUID getUUID();
    long getSeed();

    boolean shouldSaveOnNextUnload();
    void setShouldSaveOnNextUnload(boolean value);

    default void onCreate(){
        getModules().forEach(WorldModule::onCreate);
    }

    default void onInitiate(){
        getModules().forEach(WorldModule::onInitiate);
    }
    default void onLoad(){
        getModules().forEach(WorldModule::onLoad);
    }
    default void onUnload(boolean save){
        getModules().forEach(module -> module.onUnload(save));
    }
    default void onDelete(){

    }
    default void onChunkLoad(@NotNull Chunk chunk){
        getModules().forEach(module -> module.onChunkLoad(chunk));
    }
    default void onChunkUnload(@NotNull Chunk chunk){
        getModules().forEach(module -> module.onChunkUnload(chunk));
    }
    default void onChunkPopulate(@NotNull Chunk chunk){
        getModules().forEach(module -> module.onChunkPopulate(chunk));
    }
    default void onSave(){
        getModules().forEach(WorldModule::onSave);
    }

    default <T extends WorldModule> @Nullable T getModule(@NotNull Class<T> type){
        for(WorldModule module : getModules()){
            if(type.isAssignableFrom(module.getClass())) return type.cast(module);
        }
        return null;
    }

    @NotNull
    Collection<WorldModule> getModules();
}
