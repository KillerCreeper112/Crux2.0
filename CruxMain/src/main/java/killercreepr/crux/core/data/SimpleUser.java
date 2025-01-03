package killercreepr.crux.core.data;

import killercreepr.crux.api.data.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class SimpleUser implements User {
    protected final UUID uuid;
    protected final String name;

    public SimpleUser(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public @NotNull UUID uuid() {
        return uuid;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof User u)) return false;
        return u.uuid().equals(uuid);
    }

    @Override
    public String toString() {
        return "SimpleUser{uuid=" + uuid + ", name=" + name + "}";
    }
}
