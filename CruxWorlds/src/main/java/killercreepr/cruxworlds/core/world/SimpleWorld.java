package killercreepr.cruxworlds.core.world;

import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.persistence.PersistenceComponentHandler;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.creator.CruxWorldModuleCreator;
import killercreepr.cruxworlds.api.world.module.WorldModule;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class SimpleWorld implements CruxWorld, PersistenceComponentHandler, Reloadable, ManagedTicked {
    protected final @NotNull World world;
    protected final @NotNull Random random;
    protected final @NotNull Collection<WorldModule> modules;
    protected final @NotNull Collection<Ticked> tickedModules;

    public SimpleWorld(@NotNull World world, @NotNull Collection<CruxWorldModuleCreator> modules) {
        this(world, new Random(world.getSeed()), modules);
    }

    public SimpleWorld(@NotNull World world, @NotNull Random random, @NotNull Collection<CruxWorldModuleCreator> modules) {
        this.world = world;
        this.random = random;
        this.modules = new HashSet<>();
        this.tickedModules = new HashSet<>();
        modules.forEach(creator ->{
            WorldModule module = creator.create(this);
            this.modules.add(module);
            if(module instanceof Ticked t) tickedModules.add(t);
        });

    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        for(WorldModule module : modules){
            if(module instanceof Reloadable r){
                r.reload(plugin);
            }
        }
    }

    protected boolean saveOnNextUnload = true;

    @Override
    public void tick(){
        tickedModules.forEach(Ticked::tick);
    }

    @Override
    public void onLoad() {
        CruxWorld.super.onLoad();
        //active = true;
        //buildRunnable().runTaskTimerAsynchronously(Crux.getMainPlugin(), tickInterval(), tickInterval());
    }

    @Override
    public void onUnload(boolean save) {
        CruxWorld.super.onUnload(save);
        //active = false;
    }

    @Override
    public @NotNull World toBukkitWorld() {
        return world;
    }

    @Override
    public @NotNull Random getRandom() {
        return random;
    }

    /*@Override
    public @NotNull String getName() {
        return world.getName();
    }

    @Override
    public @NotNull UUID getUUID() {
        return world.getUID();
    }*/

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public boolean shouldSaveOnNextUnload() {
        return saveOnNextUnload;
    }

    @Override
    public void setShouldSaveOnNextUnload(boolean value) {
        this.saveOnNextUnload = value;
    }

    @Override
    public @NotNull Collection<WorldModule> getModules() {
        return modules;
    }

    @Override
    public void addModule(WorldModule module) {
        modules.add(module);
        if(module instanceof Ticked t) tickedModules.add(t);
    }

    @Override
    public @Nullable PersistentDataContainer getComponentsPersistentContainer() {
        return world.getPersistentDataContainer();
    }

    @Override
    public void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data) {
        CruxPersist.COMPONENTS.set(getComponentsPersistentContainer(), data.isEmpty() ? null : data);
    }

    @Override
    public void clearComponents() {
        CruxPersist.COMPONENTS.remove(getComponentsPersistentContainer());
    }

    @Override
    public @NotNull Key key() {
        return world.key();
    }
}
