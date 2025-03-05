package killercreepr.cruxtickables.core.equipment;

import killercreepr.cruxtickables.api.equipment.SetBonus;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SubSetBonus implements SetBonus {
    protected final Key key;

    public SubSetBonus(Key key) {
        this.key = key;
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
