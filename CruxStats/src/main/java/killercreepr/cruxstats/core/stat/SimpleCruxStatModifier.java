package killercreepr.cruxstats.core.stat;

import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleCruxStatModifier implements CruxStatModifier {
    protected final @NotNull Key key;
    protected final double amount;
    protected final CruxStat.Operation operation;

    public SimpleCruxStatModifier(@NotNull Key key, double amount, CruxStat.Operation operation) {
        this.key = key;
        this.amount = amount;
        this.operation = operation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, amount, operation);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof CruxStatModifier m)) return false;
        return m.key().equals(key()) && m.getAmount() == getAmount() && m.getOperation() == getOperation();
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public @NotNull CruxStat.Operation getOperation() {
        return operation;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
