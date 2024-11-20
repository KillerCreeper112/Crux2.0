package killercreepr.cruxentities.combat;

import killercreepr.cruxentities.api.combat.CruxDamageCause;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleDamageCause implements CruxDamageCause {
    protected final Key key;
    public SimpleDamageCause(Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
