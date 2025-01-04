package killercreepr.cruxstats.core.stat;

import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.PlayerDataHolder;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatInstance;
import killercreepr.cruxstats.api.stat.EntityStatHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCruxStatHolder extends PlayerDataHolder implements EntityStatHolder {
    public static final Key KEY = Crux.key("stat");
    protected final EntityStatHolder data;

    public PlayerCruxStatHolder(@NotNull Key key, @NotNull PlayerMemory parent) {
        super(key, parent);
        this.data = new DataStatHolder(parent.getUUID());
        load();
    }

    public PlayerCruxStatHolder(@NotNull PlayerMemory parent) {
        this(KEY, parent);
    }

    @Override
    protected void removingFromMemory(@Nullable Entity e) {
        super.removingFromMemory(e);
        save();
    }

    @Override
    public @Nullable CruxStatInstance getStat(@NotNull CruxStat stat) {
        return data.getStat(stat);
    }

    @Override
    public @NotNull CruxStatInstance getOrLoadStat(@NotNull CruxStat stat) {
        return data.getOrLoadStat(stat);
    }

    @Override
    public void save() {
        data.save();
    }

    @Override
    public void load() {
        data.load();
    }

    @Override
    public @NotNull String getName() {
        Player p = getPlayer();
        if(p == null) return parent.getUUID().toString();
        return p.getName();
    }
}
