package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "entity_type")
public class FileEntityType extends SimpleFileHandler<EntityType> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull EntityType object) {
        return new FilePrimitive(object.key().asMinimalString());
    }

    @Override
    public @Nullable EntityType deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        return Registry.ENTITY_TYPE.get(Key.key(s.getAsString()));
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "entity_type";
    }
}
