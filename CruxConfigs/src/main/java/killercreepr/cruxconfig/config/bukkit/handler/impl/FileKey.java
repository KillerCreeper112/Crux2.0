package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileKey extends SimpleFileHandler<Key> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull Key object) {
        return new FilePrimitive(object.asString());
    }

    @Override
    public @Nullable Key deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        return Crux.key(s.getAsString());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "key";
    }
}