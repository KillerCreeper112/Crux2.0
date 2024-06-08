package killercreepr.cruxitems.item.plugin;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class GenericPluginItem implements PluginItem{
    protected final @NotNull Key key;
    public GenericPluginItem(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
