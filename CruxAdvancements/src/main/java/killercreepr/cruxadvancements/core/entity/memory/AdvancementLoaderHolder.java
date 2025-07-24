package killercreepr.cruxadvancements.core.entity.memory;

import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.core.entity.memory.PlayerTickedDataHolder;
import killercreepr.cruxadvancements.core.config.loader.CfgCrazyAdvancementManagerCfgLoader;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AdvancementLoaderHolder extends PlayerTickedDataHolder {
    protected final CfgCrazyAdvancementManagerCfgLoader cfgManagerLoader;
    public AdvancementLoaderHolder(@NotNull Key key, @NotNull PlayerMemory parent, CfgCrazyAdvancementManagerCfgLoader cfgManagerLoader) {
        super(key, parent);
        this.cfgManagerLoader = cfgManagerLoader;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AdvancementLoaderHolder loader){
            return loader.key().equals(key);
        }
        return false;
    }

    protected boolean loaded = false;
    protected byte tick = 0;
    @Override
    public boolean shouldRemoveFromMemory(@Nullable Player e) {
        return super.shouldRemoveFromMemory(e) || loaded;
    }

    @Override
    public void adding() {
        super.adding();
        AdvancementHolder holder = parent.getDataHolder(AdvancementHolder.class);
        Bukkit.broadcastMessage("ADDING " + holder);
        if(holder == null) return;
        holder.addLoader(this);
    }

    @Override
    protected void removingFromMemory(@Nullable Player e) {
        super.removingFromMemory(e);
        AdvancementHolder holder = parent.getDataHolder(AdvancementHolder.class);
        Bukkit.broadcastMessage("REMOVING " + holder);
        if(holder == null) return;
        holder.removeLoader(this);
    }

    @Override
    protected void onTick(@NotNull Player e) {
        super.onTick(e);
        tick++;
        if(tick == 20){
            cfgManagerLoader.load(e);
            setLoaded(true);
        }
    }

    public byte getTick() {
        return tick;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
