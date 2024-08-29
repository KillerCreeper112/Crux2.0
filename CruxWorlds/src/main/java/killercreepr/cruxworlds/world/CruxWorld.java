package killercreepr.cruxworlds.world;

import killercreepr.cruxworlds.world.module.WorldModule;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

public interface CruxWorld {
    @NotNull
    World toBukkitWorld();
    @NotNull
    Random getRandom();
    @NotNull String getName();
    @NotNull UUID getUUID();

    default void onInitiate(){
        getModules().forEach(WorldModule::onInitiate);
    }
    default void onLoad(){
        getModules().forEach(WorldModule::onLoad);
    }
    default void onUnload(){
        getModules().forEach(WorldModule::onUnload);

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
