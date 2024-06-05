package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "material")
public class FileMaterial extends SimpleFileHandler<Material> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull Material object) {
        return new FilePrimitive(object.toString().toLowerCase());
    }

    @Override
    public @Nullable Material deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        return Material.matchMaterial(s.getAsString());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "material";
    }
}
