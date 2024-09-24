package killercreepr.cruxmenus.core.menu.data;

import killercreepr.cruxmenus.api.menu.data.ItemDataParser;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleItemDataParser implements ItemDataParser {
    protected final @NotNull Key key;
    public SimpleItemDataParser(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
