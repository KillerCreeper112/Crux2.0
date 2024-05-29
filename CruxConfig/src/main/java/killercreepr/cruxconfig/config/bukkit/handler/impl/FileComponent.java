package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "component")
public class FileComponent extends SimpleFileHandler<Component> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull Component object) {
        return new FilePrimitive(Crux.FORMAT.serialize(object));
    }

    @Override
    public @Nullable Component deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s)) return null;
        return Crux.FORMAT.deserialize(s.getAsString());
    }
}
