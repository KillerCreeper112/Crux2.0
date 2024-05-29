package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileComponent extends SimpleFileHandler<Component> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull Component object) {
        return new FilePrimitive(Crux.FORMAT.serialize(object));
    }

    @Override
    public @Nullable Component deserializeFromFile(@NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s)) return null;
        return Crux.FORMAT.deserialize(s.getAsString());
    }
}
