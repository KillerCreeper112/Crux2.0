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
        Map<CruxStatistic<?>, Integer> stats = data.deserialize("statistics", new TypeToken<Map<CruxStatistic, Integer>>(){}.getType());
        data.close();
        if(stats == null || stats.isEmpty()) return;
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

    @Override
    public <T> void setStatistic(@NotNull CruxStatisticType<T> type, T value, int amount) {
        setStatistic(type.get(value), amount);
    }

    @Override
    public void setStatistic(@NotNull CruxStatisticType<?> type, int amount) {
        setStatistic(type.get(null), amount);
    }

    @Override
    public void setStatistic(@NotNull CruxStatistic<?> type, int amount) {
        stats.compute(type, (stat, value) -> amount == 0 ? null : amount);
    }

    @Override
    public <T> void incrementStatistic(@NotNull CruxStatisticType<T> type, T value, int amount) {
        incrementStatistic(type.get(value), amount);
    }

    @Override
    public void incrementStatistic(@NotNull CruxStatisticType<?> type, int amount) {
        incrementStatistic(type.get(null), amount);
    }

    @Override
    public void incrementStatistic(@NotNull CruxStatistic<?> type, int amount) {
        setStatistic(type, getStatistic(type) + amount);
    }

    @Override
    public <T> void decrementStatistic(@NotNull CruxStatisticType<T> type, T value, int amount) {
        decrementStatistic(type.get(value), amount);
    }

    @Override
    public void decrementStatistic(@NotNull CruxStatisticType<?> type, int amount) {
        decrementStatistic(type.get(null), amount);
    }

    @Override
    public void decrementStatistic(@NotNull CruxStatistic<?> type, int amount) {
        setStatistic(type, getStatistic(type) - amount);
    }
}
