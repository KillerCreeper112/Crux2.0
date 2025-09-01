package killercreepr.cruxconfig.config.bukkit.handler.impl;

import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileAttribute extends FileGenericKeyed<Attribute>{
    @Override
    public @Nullable Attribute deserializeFromKey(@NotNull Key key) {
        return Registry.ATTRIBUTE.get(key);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "attribute";
    }
}
