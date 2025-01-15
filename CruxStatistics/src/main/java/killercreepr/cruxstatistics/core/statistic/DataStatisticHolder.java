package killercreepr.cruxstatistics.core.statistic;

import com.google.common.reflect.TypeToken;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;
import killercreepr.cruxstatistics.api.statistic.EntityStatisticHolder;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class DataStatisticHolder implements EntityStatisticHolder {
    protected final Object2IntMap<CruxStatistic<?>> stats = Object2IntMaps.synchronize(new Object2IntOpenHashMap<>());
    protected final UUID uuid;

    public DataStatisticHolder(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public @NotNull String getName() {
        OfflinePlayer offline = Crux.getServer().getOfflinePlayer(uuid);
        String name = offline.getName();
        return name == null ? uuid.toString() : name;
    }

    public CruxJson getDataFile(){
        return new CruxJson(Crux.getMainPlugin(), "data/cruxstatistics/player/" + uuid);
    }

    @Override
    public void save() {
        CruxJson data = getDataFile();
        data.serialize("statistics", stats);
        data.save();
    }

    @Override
    public void load() {
        CruxJson data = getDataFile();
        Map<CruxStatistic<?>, Integer> stats = data.deserialize("statistics", new TypeToken<Map<CruxStatistic<?>, Integer>>(){}.getType());
        data.close();
        if(stats == null) return;
        this.stats.putAll(stats);
    }

    @Override
    public int getStatistic(@NotNull CruxStatisticType<?> type) {
        return getStatistic(type.get(null));
    }

    @Override
    public int getStatistic(@NotNull CruxStatistic<?> statistic) {
        return stats.getOrDefault(statistic, 0);
    }

    @Override
    public <T> int getStatistic(@NotNull CruxStatisticType<T> type, T value) {
        return getStatistic(type.get(value));
    }
}
