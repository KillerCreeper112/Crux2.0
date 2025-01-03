package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.data.User;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
@JsonSerializer(id = "user")
public class FileUser extends SimpleFileHandler<User> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull User user) {
        FileRegistry reg =context.getRegistry();
        return new FileObject()
            .add("uuid", reg.serializeToFile(user.uuid()))
            .add("name", reg.serializeToFile(user.name()));
    }

    @Override
    public @Nullable User deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg =context.getRegistry();
        UUID uuid = reg.deserializeFromFile(UUID.class, o.get("uuid"));
        String name = reg.deserializeFromFile(String.class, o.get("name"));
        if(uuid == null || name == null) return null;
        return User.user(uuid, name);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "user";
    }
}
