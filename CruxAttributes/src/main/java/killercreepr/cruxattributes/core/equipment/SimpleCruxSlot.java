package killercreepr.cruxattributes.core.equipment;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SimpleCruxSlot implements CruxSlot {
    protected final Key key;
    protected final int index;

    public SimpleCruxSlot(Key key, int index) {
        this.key = key;
        this.index = index;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public @NotNull Key attributeKey(){
        return CruxAttribute.k("slot." + key.value());
    }

    @Override
    public boolean wouldActivate(@NotNull Entity p, int slot) {
        return slot == index;
    }

    @Override
    public int getIndex(@NotNull Entity p) {
        return index;
    }

    @Override
    public @NotNull String translateKey() {
        return "cruxslot/" + key.asString();
    }
}
