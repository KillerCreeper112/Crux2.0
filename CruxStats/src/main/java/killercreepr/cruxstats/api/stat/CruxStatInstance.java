package killercreepr.cruxstats.api.stat;

import killercreepr.crux.valueproviders.number.NumberHolder;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstats.core.stat.SimpleCruxStatInstance;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxStatInstance {
    static CruxStatInstance instance(@NotNull CruxStat stat, @NotNull NumberHolder base){
        return new SimpleCruxStatInstance(stat, base);
    }

    static CruxStatInstance instance(@NotNull CruxStat stat){
        return instance(stat, NumberProvider.constant(0D));
    }

    @NotNull CruxStat getStat();
    double getBaseValue();
    double getValue();
    @NotNull
    Collection<CruxStatModifier> getModifiers();
    @NotNull
    Collection<CruxStatModifier> clearModifiers();
    void addModifier(@NotNull CruxStatModifier modifier);
    @Nullable CruxStatModifier
    removeModifier(@NotNull Key key);
    @Nullable CruxStatModifier getModifier(@NotNull Key key);
    boolean hasModifier(@NotNull Key key);
}
