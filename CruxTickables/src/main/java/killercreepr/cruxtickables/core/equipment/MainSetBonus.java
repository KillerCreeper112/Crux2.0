package killercreepr.cruxtickables.core.equipment;

import com.google.common.base.Preconditions;
import killercreepr.cruxtickables.api.equipment.SetBonus;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainSetBonus implements SetBonus {
    protected final Key key;
    protected final int equipmentAmount;

    public MainSetBonus(Key key, int equipmentAmount) {
        Preconditions.checkArgument(equipmentAmount > 0, "EquipmentAmount must be greater than 0!");
        this.key = key;
        this.equipmentAmount = equipmentAmount;
    }

    @Override
    public String toString() {
        return "MainSetBonus{" +
            "key=" + key +
            ", equipmentAmount=" + equipmentAmount +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MainSetBonus that)) return false;
        return equipmentAmount == that.equipmentAmount && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, equipmentAmount);
    }

    @Override
    public int getEquipmentAmount() {
        return equipmentAmount;
    }

    @Override
    public boolean isMain() {
        return true;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
