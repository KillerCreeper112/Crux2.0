package killercreepr.cruxstats.core.stat;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.PlayerDataHolder;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatHolder;
import killercreepr.cruxstats.api.stat.CruxStatInstance;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerCruxStatHolder extends PlayerDataHolder implements CruxStatHolder {
    public static final Key KEY = Crux.key("stat");
    protected final @NotNull Map<CruxStat, CruxStatInstance> stats = new HashMap<>();

    public PlayerCruxStatHolder(@NotNull Key key, @NotNull PlayerMemory parent) {
        super(key, parent);
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
        return stats.get(stat);
    }

    @Override
    public @NotNull CruxStatInstance getOrLoadStat(@NotNull CruxStat stat) {
        CruxStatInstance instance = getStat(stat);
        if(instance != null) return instance;
        instance = stat.createNewInstance();
        if(instance == null) throw new UnsupportedOperationException(stat.key() + " cannot be create new instances!");
        stats.put(instance.getStat(), instance);
        return instance;
    }

    public CruxJson getDataFile(){
        return new CruxJson(Crux.getMainPlugin(), "data/cruxstats/player/" + parent.getUUID());
    }

    @Override
    public void save() {
        CruxJson data = getDataFile();
        data.serialize("stats", stats.values());
        data.save();
    }

    @Override
    public void load() {
        CruxJson data = getDataFile();
        Collection<CruxStatInstance> stats = data.deserialize("stats", new TypeToken<Collection<CruxStatInstance>>(){}.getType());
        data.close();
        if(stats == null) return;
        stats.forEach(stat -> this.stats.put(stat.getStat(), stat));
    }
}
