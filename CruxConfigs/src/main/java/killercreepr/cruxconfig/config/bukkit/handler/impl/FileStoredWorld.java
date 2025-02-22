package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.data.world.StoredWorld;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "stored_world")
public class FileStoredWorld implements FileObjectHandler<StoredWorld> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull StoredWorld obj) {
        return ctx.getRegistry().serializeToFile(obj.worldKey());
    }

    @Override
    public @Nullable StoredWorld deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        Key key = ctx.getRegistry().deserializeFromFile(Key.class, e);
        if(key == null) return null;
        return StoredWorld.storedWorld(key);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "stored_world";
    }
}
