package killercreepr.crux.data.entity;

import killercreepr.crux.data.Holder;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public interface EntityMemory extends Holder<Entity> {
    MappedRegistry<Plugin, Set<Consumer<EntityMemory>>> ADD_FUNCTIONS = new SimpleMappedRegistry<>();
    static @NotNull Consumer<EntityMemory> registerFunction(@NotNull Plugin plugin, @NotNull Consumer<EntityMemory> function){
        Set<Consumer<EntityMemory>> data = ADD_FUNCTIONS.getOrDefault(plugin, new HashSet<>());
        data.add(function);
        ADD_FUNCTIONS.register(plugin, data);
        return function;
    }

    static boolean removeFunction(@NotNull Plugin plugin, @NotNull Consumer<EntityMemory> function){
        Set<Consumer<EntityMemory>> data = ADD_FUNCTIONS.getOrDefault(plugin, new HashSet<>());
        boolean b = data.remove(function);
        if(data.isEmpty()) ADD_FUNCTIONS.remove(plugin);
        else ADD_FUNCTIONS.register(plugin, data);
        return b;
    }

    static @NotNull MappedRegistry<Plugin, Set<Consumer<EntityMemory>>> getAddFunctions(){ return ADD_FUNCTIONS; }

    EntityMemoryRegistry<EntityMemory> REGISTRY = new EntityMemoryRegistry<>();

    static @Nullable EntityMemory get(@NotNull Entity p){
        if(p instanceof Player player) return PlayerMemory.get(player);
        return get(p.getUniqueId());
    }

    static @NotNull EntityMemory getOrCreate(@NotNull Entity p){
        if(p instanceof Player player) return PlayerMemory.getOrCreate(player);
        EntityMemory d = get(p.getUniqueId());
        if(d == null){
            d = register(new SimpleEntityMemory(p));
        }
        return d;
    }
    static <T extends EntityMemory> T register(@NotNull T tick){
        for(Map.Entry<Plugin, Set<Consumer<EntityMemory>>> entry : ADD_FUNCTIONS.entrySet()){
            for(Consumer<EntityMemory> d : entry.getValue()){
                d.accept(tick);
            }
        }
        REGISTRY.register(tick.getUUID(), tick);
        return tick;
    }

    static boolean register(@NotNull EntityMemory tick, boolean override){
        if(!override && REGISTRY.containsKey(tick.getUUID())) return false;
        register(tick);
        return true;
    }

    static @Nullable EntityMemory get(@NotNull UUID uuid){
        return REGISTRY.get(uuid);
    }

    static <T extends DataHolder> @Nullable T getDataHolder(@NotNull Entity entity, @NotNull Class<T> clazz){
        return getDataHolder(entity.getUniqueId(), clazz);
    }

    static <T extends DataHolder> @Nullable T getDataHolder(@NotNull UUID uuid, @NotNull Class<T> clazz){
        EntityMemory mem = get(uuid);
        if(mem == null) return null;
        return mem.getDataHolder(clazz);
    }

    static <T extends DataHolder> @Nullable T getOrCreateDataHolder(@NotNull Entity entity, @NotNull Class<T> clazz){
        EntityMemory mem = getOrCreate(entity);
        return mem.getDataHolder(clazz);
    }

    @NotNull KeyedRegistry<DataHolder> getDataHolders();
    @Nullable DataHolder getDataHolder(@NotNull Key key);

    default <T extends DataHolder> @Nullable T getDataHolder(@NotNull Class<T> clazz){
        try{
            Field staticKeyField = clazz.getDeclaredField("KEY");
            Key key = (Key) staticKeyField.get(null);
            try{
                return clazz.cast(getDataHolder(key));
            }catch (NullPointerException | ClassCastException ignored){ return null; }
        }catch (NoSuchFieldException | IllegalAccessException ignored){ return null; }
    }


    default  <T extends DataHolder> @NotNull T getDataHolderOrThrow(@NotNull Class<T> clazz){
        T value = getDataHolder(clazz);
        if(value == null) throw new IllegalStateException("EntityMem does not have DataHolder specified! " + clazz);
        return value;
    }

    @NotNull UUID getUUID();

    /**
     * @return Whether to remove this from memory.
     */
    boolean tick();

    void removeDataHolders(@Nullable Entity e);
}
