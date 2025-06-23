package killercreepr.cruxtickables.core.equipment;

import killercreepr.cruxtickables.api.equipment.SetBonus;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SubSetBonus implements SetBonus {
    protected final Key key;

    public SubSetBonus(Key key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SubSetBonus that)) return false;
        return Objects.equals(key, that.key);
    }

    @Override
    public String toString() {
        return "SubSetBonus{" +
            "key=" + key +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public int getEquipmentAmount() {
        return 0;
    }

    @Override
    public boolean isMain() {
        return false;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
