package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.GameRule;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "game_rule")
public class FileGameRule extends SimpleFileHandler<GameRule<?>> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull GameRule<?> object) {
        return new FilePrimitive(object.key().asString());
    }

    @Override
    public @Nullable GameRule<?> deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        var key = context.getRegistry().deserializeFromFile(Key.class, e);
        if(key == null) return null;
        return Registry.GAME_RULE.get(key);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "game_rule";
    }
}
