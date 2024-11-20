package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.Crux;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileItemHolder extends FileGenericKeyed<ItemHolder>{
    @Override
    public @Nullable ItemHolder deserializeFromKey(@NotNull Key key) {
        return Crux.handlers().item().getItem(key);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_holder";
    }
}
