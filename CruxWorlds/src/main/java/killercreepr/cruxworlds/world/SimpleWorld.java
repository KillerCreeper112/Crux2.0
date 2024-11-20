package killercreepr.cruxworlds.world;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.cruxworlds.world.creator.CruxWorldModuleCreator;
import killercreepr.cruxworlds.world.module.WorldModule;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

public class SimpleWorld implements CruxWorld {
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

    protected boolean active = false;

    public BukkitRunnable buildRunnable(){
        return new BukkitRunnable() {
            @Override
            public void run() {
                if(!isActive()){
                    cancel();
                    return;
                }
                tick();
            }
        };
    }

    public boolean isActive(){
        return active;
    }

    public void tick(){
        tickedModules.forEach(Ticked::tick);
    }

    public int tickInterval(){
        return 100;
    }

    @Override
    public void onLoad() {
        CruxWorld.super.onLoad();
        active = true;
        buildRunnable().runTaskTimerAsynchronously(Crux.getMainPlugin(), tickInterval(), tickInterval());
    }

    @Override
    public void onUnload() {
        CruxWorld.super.onUnload();
        active = false;
    }

    @Override
    public @NotNull World toBukkitWorld() {
        return world;
    }

    @Override
    public @NotNull Random getRandom() {
        return random;
    }

    @Override
    public @NotNull String getName() {
        return world.getName();
    }

    @Override
    public @NotNull UUID getUUID() {
        return world.getUID();
    }

    @Override
    public @NotNull Collection<WorldModule> getModules() {
        return modules;
    }
}
