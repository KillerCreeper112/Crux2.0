package killercreepr.crux.api.util;

import killercreepr.crux.api.data.User;
import killercreepr.crux.core.util.SimpleUserMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface UserMap<E extends User, T> extends Map<E, T> {
    static <E extends User, T> UserMap<E, T> userMap(Map<E, T> usersToObject, Map<UUID, E> users, Map<String, E> usersByName){
        return new SimpleUserMap<>(usersToObject, users, usersByName);
    }
    static <E extends User, T> UserMap<E, T> userMap(){
        return userMap(() -> new HashMap<>());
    }
    static <E extends User, T> UserMap<E, T> userMap(Supplier<Map> mapSupplier){
        return new SimpleUserMap<>(mapSupplier.get(), mapSupplier.get(), mapSupplier.get());
    }

    E getUser(UUID uuid);
    E getUser(String name);
    boolean hasUser(UUID uuid);
    boolean hasUser(String name);

    default void applyUser(UUID uuid, Consumer<E> consumer) {
        E user = getUser(uuid);
        if(user != null) consumer.accept(user);
    }

    default void applyUser(String name, Consumer<E> consumer) {
        E user = getUser(name);
        if(user != null) consumer.accept(user);
    }
}
