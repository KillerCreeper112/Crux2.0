package killercreepr.crux.api.data;

import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.SimpleUser;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface User {
    static User user(@NotNull Entity entity){
        return user(entity.getUniqueId(), entity.getName());
    }
    static User user(@NotNull OfflinePlayer entity){
        return user(entity.getUniqueId(), entity.getName());
    }
    static User user(UUID uuid, String name){
        return new SimpleUser(uuid, name);
    }
    static User userName(String name){
        return new SimpleUser(null, name);
    }
    static User attemptUserFromString(String string, boolean useOfflinePlayer){
        try{
            UUID uuid = UUID.fromString(string);
            String name = useOfflinePlayer ? Crux.getServer().getOfflinePlayer(uuid).getName() : null;
            return User.user(uuid, name);
        }catch (IllegalArgumentException ignored){}
        return userName(string);
    }

    UUID uuid();
    String name();
}
