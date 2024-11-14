package killercreepr.cruxstats.api.stat;

import killercreepr.cruxstats.core.stat.SimpleCruxStatModifier;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface CruxStatModifier extends Keyed {
    static CruxStatModifier modifier(@NotNull Key key, double amount, @NotNull CruxStat.Operation operation){
        return new SimpleCruxStatModifier(key, amount, operation);
    }
    static CruxStatModifier addModifier(@NotNull Key key, double amount){
        return modifier(key, amount, CruxStat.Operation.ADD);
    }
    static CruxStatModifier setModifier(@NotNull Key key, double amount){
        return modifier(key, amount, CruxStat.Operation.SET);
    }
    static CruxStatModifier multiplyModifier(@NotNull Key key, double amount){
        return modifier(key, amount, CruxStat.Operation.MULTIPLY);
    }
    double getAmount();
    @NotNull CruxStat.Operation getOperation();
}
