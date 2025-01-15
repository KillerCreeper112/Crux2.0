package killercreepr.cruxstatistics.core.statistic;

import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.PlayerDataHolder;
import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;
import killercreepr.cruxstatistics.api.statistic.EntityStatisticHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCruxStatisticHolder extends PlayerDataHolder implements EntityStatisticHolder {
    public static final Key KEY = Crux.key("statistics");
    protected final EntityStatisticHolder data;

    public PlayerCruxStatisticHolder(@NotNull Key key, @NotNull PlayerMemory parent) {
        super(key, parent);
        this.data = new DataStatisticHolder(parent.getUUID());
        load();
    }

    public PlayerCruxStatisticHolder(@NotNull PlayerMemory parent) {
        this(KEY, parent);
    }

    @Override
    protected void removingFromMemory(@Nullable Entity e) {
        super.removingFromMemory(e);
        save();
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

    @Override
    public int getStatistic(@NotNull CruxStatisticType<?> type) {
        return data.getStatistic(type);
    }

    @Override
    public int getStatistic(@NotNull CruxStatistic<?> statistic) {
        return data.getStatistic(statistic);
    }

    @Override
    public <T> int getStatistic(@NotNull CruxStatisticType<T> type, T value) {
        return data.getStatistic(type, value);
    }
}
