package killercreepr.cruxstats.api.stat;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxStatInstance {
    @NotNull CruxStat getStat();
    double getBaseValue();
    double getValue();
    @NotNull
    Collection<CruxStatModifier> getModifiers();
    void addModifier(@NotNull CruxStatModifier modifier);
    @Nullable CruxStatModifier
    removeModifier(@NotNull Key key);
    @Nullable CruxStatModifier getModifier(@NotNull Key key);
    boolean hasModifier(@NotNull Key key);
}
