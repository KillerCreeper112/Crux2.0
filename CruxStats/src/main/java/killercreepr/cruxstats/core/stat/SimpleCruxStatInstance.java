package killercreepr.cruxstats.core.stat;

import killercreepr.crux.valueproviders.number.NumberHolder;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatInstance;
import killercreepr.cruxstats.api.stat.CruxStatModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleCruxStatInstance implements CruxStatInstance {
    protected final @NotNull CruxStat stat;
    protected final @NotNull NumberHolder base;
    protected final @NotNull Map<Key, CruxStatModifier> modifiers = new HashMap<>();

    protected double cache;
    protected boolean dirty = true;

    public SimpleCruxStatInstance(@NotNull CruxStat stat, @NotNull NumberHolder base) {
        this.stat = stat;
        this.base = base;
    }

    @Override
    public @NotNull CruxStat getStat() {
        return stat;
    }

    @Override
    public double getBaseValue() {
        return base.value().doubleValue();
    }

    @Override
    public double getValue() {
        if(dirty){
            dirty = false;
            cache = calculateValue();
        }
        return cache;
    }

    public void setDirty(){
        this.dirty = true;
    }

    public double calculateValue(){
        double x = getBaseValue();
        double multiply = 0D;
        Collection<CruxStatModifier> ADD = new ArrayList<>();
        Set<Key> SET = new HashSet<>();
        for(CruxStatModifier m : modifiers.values()){
            switch (m.getOperation()){
                case CruxStat.Operation.MULTIPLY -> multiply += m.getAmount();
                case CruxStat.Operation.SET ->{
                    SET.add(m.key());
                    x = m.getAmount();
                }
                case CruxStat.Operation.ADD -> ADD.add(m);
            }
        }
        for(CruxStatModifier m : ADD){
            if(SET.contains(m.key())) continue;
            x += m.getAmount();
        }
        if(multiply != 0D) x *= (1D + multiply);
        return stat.processValue(x);
    }

    @Override
    public @NotNull Collection<CruxStatModifier> getModifiers() {
        return modifiers.values();
    }

    @Override
    public @NotNull Collection<CruxStatModifier> clearModifiers() {
        Collection<CruxStatModifier> removed = new HashSet<>(getModifiers());
        modifiers.clear();
        setDirty();
        return removed;
    }

    @Override
    public void addModifier(@NotNull CruxStatModifier modifier) {
        modifiers.put(modifier.key(), modifier);
        setDirty();
    }

    @Override
    public @Nullable CruxStatModifier removeModifier(@NotNull Key key) {
        CruxStatModifier mod = modifiers.remove(key);
        if(mod != null) setDirty();
        return mod;
    }

    @Override
    public @Nullable CruxStatModifier getModifier(@NotNull Key key) {
        return modifiers.get(key);
    }

    @Override
    public boolean hasModifier(@NotNull Key key) {
        return modifiers.containsKey(key);
    }
}
