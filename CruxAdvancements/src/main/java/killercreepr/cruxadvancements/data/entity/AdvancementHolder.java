package killercreepr.cruxadvancements.data.entity;

import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.PlayerDataHolder;
import killercreepr.crux.data.entity.PlayerMemory;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class AdvancementHolder extends PlayerDataHolder {
    public static final Key KEY = Crux.key("advancement");
    public AdvancementHolder(@NotNull PlayerMemory parent) {
        this(KEY, parent);
    }
    public AdvancementHolder(@NotNull Key key, @NotNull PlayerMemory parent) {
        super(key, parent);
    }
}
