package killercreepr.crux.api.data;

import killercreepr.crux.core.data.SimpleUser;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface User {
    static User user(@NotNull Entity entity){
        return user(entity.getUniqueId(), entity.getName());
    }
    static User user(@NotNull UUID uuid, @NotNull String name){
        return new SimpleUser(uuid, name);
    }

    @NotNull UUID uuid();
    @NotNull String name();
}
