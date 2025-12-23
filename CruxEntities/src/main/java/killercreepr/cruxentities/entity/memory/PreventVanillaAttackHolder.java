package killercreepr.cruxentities.entity.memory;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.EntityTickedDataHolder;
import killercreepr.crux.core.util.CruxMath;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PreventVanillaAttackHolder extends EntityTickedDataHolder {
    public static final Key KEY = Crux.key("prevent_vanilla_attack");
    public PreventVanillaAttackHolder(@NotNull Key key, @NotNull EntityMemory parent) {
        super(key, parent);
    }
    public PreventVanillaAttackHolder(@NotNull EntityMemory parent) {
        this(KEY, parent);
    }

    @Override
    public boolean shouldRemoveFromMemory(@Nullable Entity e) {
        return !CruxMath.hasOccurredWithin(time, 2);
    }

    public long time = System.currentTimeMillis();

    @Override
    public void tick(@NotNull Entity e) {

    }
}
