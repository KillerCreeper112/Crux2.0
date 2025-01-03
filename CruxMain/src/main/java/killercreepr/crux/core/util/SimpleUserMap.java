package killercreepr.crux.core.util;

import killercreepr.crux.api.data.User;
import killercreepr.crux.api.util.UserMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleUserMap<E extends User, T> implements UserMap<E,T> {
    protected final Map<E, T> usersToObject;
    protected final Map<UUID, E> users;
    protected final Map<String, E> usersByName;
    public SimpleUserMap(Map<E, T> usersToObject, Map<UUID, E> users, Map<String, E> usersByName) {
        this.usersToObject = usersToObject;
        this.users = users;
        this.usersByName = usersByName;
    }

    public E getUser(UUID uuid){
        return users.get(uuid);
    }

    public E getUser(String name){
        return usersByName.get(name);
    }

    public boolean hasUser(UUID uuid){
        return users.containsKey(uuid);
    }

    public boolean hasUser(String name){
        return usersByName.containsKey(name);
    }

    @Override
    public int size() {
        return usersToObject.size();
    }

    @Override
    public boolean isEmpty() {
        return usersToObject.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return usersToObject.containsKey(key);
    }


    @Override
    public boolean containsValue(Object value) {
        return usersToObject.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return usersToObject.get(key);
    }

    @Override
    public @Nullable T put(E key, T value) {
        users.put(key.uuid(), key);
        usersByName.put(key.name(), key);
        return usersToObject.put(key, value);
    }

    @Override
    public T remove(Object key) {
        if((key instanceof User u)){
            users.remove(u.uuid());
            usersByName.remove(u.name());
        }
        return usersToObject.remove(key);
    }
    @Override
    public void putAll(@NotNull Map<? extends E, ? extends T> m) {
        m.keySet().forEach(key ->{
            users.put(key.uuid(), key);
            usersByName.put(key.name(), key);
        });
        usersToObject.putAll(m);
    }

    @Override
    public void clear() {
        usersToObject.clear();
        users.clear();
        usersByName.clear();
    }

    @Override
    public @NotNull Set<E> keySet() {
        return usersToObject.keySet();
    }

    @Override
    public @NotNull Collection<T> values() {
        return usersToObject.values();
    }

    @Override
    public @NotNull Set<Entry<E, T>> entrySet() {
        return usersToObject.entrySet();
    }
}
