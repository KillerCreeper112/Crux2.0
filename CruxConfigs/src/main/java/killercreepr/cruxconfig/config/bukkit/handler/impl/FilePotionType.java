package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "potion_type")
public class FilePotionType extends SimpleFileHandler<PotionType> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull PotionType object) {
        return new FilePrimitive(object.key().asMinimalString());
    }

    @Override
    public @Nullable PotionType deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        return Registry.POTION.get(Key.key(s.getAsString()));
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "potion_type";
    }
}
