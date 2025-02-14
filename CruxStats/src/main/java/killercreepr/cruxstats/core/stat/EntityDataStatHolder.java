package killercreepr.cruxstats.core.stat;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatInstance;
import killercreepr.cruxstats.api.stat.EntityStatHolder;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityDataStatHolder implements EntityStatHolder {
    protected final @NotNull Map<CruxStat, CruxStatInstance> stats = new HashMap<>();
    protected final UUID uuid;

    public EntityDataStatHolder(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public @NotNull String getName() {
        OfflinePlayer offline = Crux.getServer().getOfflinePlayer(uuid);
        String name = offline.getName();
        return name == null ? uuid.toString() : name;
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
        if(instance == null) throw new UnsupportedOperationException(stat.key() + " cannot create new stat instances!");
        stats.put(instance.getStat(), instance);
        return instance;
    }

    public CruxJson getDataFile(){
        return new CruxJson(Crux.getMainPlugin(), "data/cruxstats/player/" + uuid);
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
