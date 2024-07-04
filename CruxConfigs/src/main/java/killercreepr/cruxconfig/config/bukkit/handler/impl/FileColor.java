package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.util.CruxColor;
import killercreepr.crux.util.CruxString;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "color")
public class FileColor extends SimpleFileHandler<Color> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull Color object) {
        return new FilePrimitive(CruxColor.colorToHex(object));
    }

    @Override
    public @Nullable Color deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        return CruxColor.hexToColor(s.getAsString());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "color";
    }
}
