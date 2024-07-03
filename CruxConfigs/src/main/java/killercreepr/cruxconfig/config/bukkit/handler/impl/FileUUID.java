package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@JsonSerializer(id = "uuid")
public class FileUUID extends SimpleFileHandler<UUID> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull UUID uuid) {
        return new FileObject()
            .addProperty("least", uuid.getLeastSignificantBits())
            .addProperty("most", uuid.getMostSignificantBits());
    }

    @Override
    public @Nullable UUID deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        Number least = registry.deserialize(Number.class, o.get("least"));
        Number most = registry.deserialize(Number.class, o.get("most"));
        if(least==null||most==null) return null;
        return new UUID(most.longValue(), least.longValue());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "uuid";
    }
}
