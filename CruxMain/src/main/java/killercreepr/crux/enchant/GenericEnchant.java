package killercreepr.crux.enchant;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class GenericEnchant implements CruxEnchant{
    protected final @NotNull Key key;
    public GenericEnchant(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
