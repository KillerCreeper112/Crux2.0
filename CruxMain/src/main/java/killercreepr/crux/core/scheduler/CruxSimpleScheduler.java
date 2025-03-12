package killercreepr.crux.core.scheduler;

import killercreepr.crux.api.scheduler.CruxScheduler;
import killercreepr.crux.core.Crux;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CruxSimpleScheduler implements CruxScheduler {
    public Plugin plugin(){
        return Crux.getMainPlugin();
    }

    public BukkitScheduler scheduler(){
        return plugin().getServer().getScheduler();
    }

    public BukkitTask dummyTask(){
        return new BukkitTask() {
            @Override
            public int getTaskId() {
                return 0;
            }
            @Override
            public @NotNull Plugin getOwner() {
                return plugin();
            }
            @Override
            public boolean isSync() {
                return true;
            }
            @Override
            public boolean isCancelled() {
                return false;
            }
            @Override
            public void cancel() {}
        };
    }

    @Override
    public CruxScheduler runTask(@NotNull Runnable task) {
        Plugin p = plugin();
        if(p.isEnabled()) scheduler().runTask(p, task);
        else task.run();
        return this;
    }

    @Override
    public CruxScheduler runTaskMain(@NotNull Runnable task) {
        if(Bukkit.isPrimaryThread()) task.run();
        else return runTask(task);
        return this;
    }

    @Override
    public CruxScheduler runTask(@NotNull Consumer<? super BukkitTask> task) {
        Plugin p = plugin();
        if(p.isEnabled()) scheduler().runTask(plugin(), task);
        else task.accept(dummyTask());
        return this;
    }

    @Override
    public CruxScheduler runTaskLater(@NotNull Runnable task, long delay) {
        Plugin p = plugin();
        if(p.isEnabled()){
            scheduler().runTaskLater(p, task, delay);
        }else task.run();
        return this;
    }

    @Override
    public CruxScheduler runTaskLater(@NotNull Consumer<? super BukkitTask> task, long delay){
        Plugin p = plugin();
        if(p.isEnabled()) scheduler().runTaskLater(plugin(), task, delay);
        else task.accept(dummyTask());
        return this;
    }

    @Override
    public CruxScheduler runTaskAsync(@NotNull Runnable task) {
        Plugin p = plugin();
        if(p.isEnabled()) scheduler().runTaskAsynchronously(p, task);
        else task.run();
        return this;
    }

    @Override
    public CruxScheduler runTaskAsync(@NotNull Consumer<? super BukkitTask> task) {
        Plugin p = plugin();
        if(p.isEnabled()) scheduler().runTaskAsynchronously(plugin(), task);
        else task.accept(dummyTask());
        return this;
    }

    @Override
    public CruxScheduler runTaskLaterAsync(@NotNull Runnable task, long delay) {
        Plugin p = plugin();
        if(p.isEnabled()){
            scheduler().runTaskLaterAsynchronously(p, task, delay);
        }else task.run();
        return this;
    }

    @Override
    public CruxScheduler runTaskLaterAsync(@NotNull Consumer<? super BukkitTask> task, long delay) {
        Plugin p = plugin();
        if(p.isEnabled()) scheduler().runTaskLaterAsynchronously(plugin(), task, delay);
        else task.accept(dummyTask());
        return this;
    }
}
