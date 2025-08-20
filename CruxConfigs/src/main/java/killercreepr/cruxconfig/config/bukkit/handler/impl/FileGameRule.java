package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.bukkit.GameRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "game_rule")
public class FileGameRule extends SimpleFileHandler<GameRule<?>> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull GameRule<?> object) {
        return new FilePrimitive(object.getName());
    }

    @Override
    public @Nullable GameRule<?> deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        return GameRule.getByName(s.getAsString());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "game_rule";
    }
}
