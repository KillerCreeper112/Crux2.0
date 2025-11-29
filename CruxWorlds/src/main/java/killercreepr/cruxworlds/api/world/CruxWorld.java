package killercreepr.cruxworlds.api.world;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxworlds.api.world.module.WorldModule;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public interface CruxWorld extends CruxKeyed, DataComponentHandler {
    @NotNull
    World toBukkitWorld();
    @NotNull
    Random getRandom();
    //@NotNull String getName();
    long getSeed();

    boolean shouldSaveOnNextUnload();
    void setShouldSaveOnNextUnload(boolean value);

    void scheduleUnload(boolean save);
    boolean scheduledUnload();
    boolean scheduledUnloadSave();

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
        getModules().forEach(module ->{
            module.onUnload(save);
        });
    }
    default void onDelete(){
        getModules().forEach(WorldModule::onDelete);
    }
    default void onPreDelete(){
        getModules().forEach(WorldModule::onPreDelete);
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

    default <T extends WorldModule> @NotNull Collection<T> getModulesOfType(@NotNull Class<T> type){
        Collection<T> list = new HashSet<>();
        for(WorldModule module : getModules()){
            if(type.isAssignableFrom(module.getClass())) list.add(type.cast(module));
        }
        return list;
    }

    @NotNull
    Collection<WorldModule> getModules();
    @ApiStatus.Experimental
    void addModule(WorldModule module);
}
