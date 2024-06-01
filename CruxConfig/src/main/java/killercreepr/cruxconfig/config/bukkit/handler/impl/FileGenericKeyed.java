package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FileGenericKeyed<T extends Keyed> extends SimpleFileHandler<T> {
    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        Key key = parseKey(s);
        if(key==null) return null;
        return deserializeFromKey(key);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        return new FilePrimitive(object.key().asString());
    }

    public abstract @Nullable T deserializeFromKey(@NotNull Key key);

    public @Nullable Key parseKey(@NotNull FilePrimitive e){
        return Key.key(e.getAsString());
    }
}
