package killercreepr.cruxadvancements.data;

import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.registries.AdvancementRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TrackedAdvancement {
    protected final @NotNull Key managerKey;
    protected final @NotNull Key advancementKey;
    protected final boolean isGlobal;
    public TrackedAdvancement(@NotNull Key managerKey, @NotNull Key advancementKey, boolean isGlobal) {
        this.managerKey = managerKey;
        this.advancementKey = advancementKey;
        this.isGlobal = isGlobal;
    }

    public TrackedAdvancement(@NotNull Key managerKey, @NotNull Key advancementKey) {
        this(managerKey, advancementKey, false);
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public CruxAdvancementManager<?> getManager(){
        return AdvancementRegistries.ADVANCEMENT_MANAGERS.get(managerKey);
    }

    public ObjectiveAdvancement getObjective(){
        return getAdvancementOrThrow(ObjectiveAdvancement.class);
    }

    public CruxAdvancement getAdvancement(){
        CruxAdvancementManager<?> manager = getManager();
        if(manager==null) return null;
        return manager.getAdvancement(advancementKey);
    }

    public CruxAdvancement getAdvancementOrThrow(){
        CruxAdvancementManager<?> manager = getManager();
        Objects.requireNonNull(manager, "CruxAdvancementManager not found! " + this);
        CruxAdvancement a = getAdvancement();
        Objects.requireNonNull(a, "CruxAdvancement not found! " + this);
        return a;
    }

    public <T extends CruxAdvancement> T getAdvancement(@NotNull Class<T> type){
        CruxAdvancement a = getAdvancement();
        if(a==null || !type.isAssignableFrom(a.getClass())) return null;
        return type.cast(a);
    }

    public <T extends CruxAdvancement> T getAdvancementOrThrow(@NotNull Class<T> type){
        CruxAdvancementManager<?> manager = getManager();
        Objects.requireNonNull(manager, "CruxAdvancementManager not found! " + this);
        T a = getAdvancement(type);
        if(a == null){
            if(getAdvancement() == null) Objects.requireNonNull(a, "CruxAdvancement not found! (" + type + ") " + this);
            else Objects.requireNonNull(a, "CruxAdvancement not assignable to " + type + " (" + getAdvancement() + ")" + this);
        }
        return a;
    }

    public @NotNull Key getManagerKey() {
        return managerKey;
    }

    public @NotNull Key getAdvancementKey() {
        return advancementKey;
    }

    @Override
    public String toString() {
        return "TrackedAdvancement{managerKey=" + managerKey + ", advancementKey=" + advancementKey + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof TrackedAdvancement other)) return false;
        return other.getManagerKey().equals(managerKey) && other.getAdvancementKey().equals(advancementKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(managerKey, advancementKey);
    }
}
