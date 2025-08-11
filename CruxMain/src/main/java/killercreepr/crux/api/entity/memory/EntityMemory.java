package killercreepr.crux.api.entity.memory;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.entity.memory.EntityMemoryRegistry;
import killercreepr.crux.core.entity.memory.SimpleEntityMemory;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

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
    EntityMemoryRegistry<EntityMemory> MAIN_THREAD_REGISTRY = new EntityMemoryRegistry<>(new HashMap<>());

    static @Nullable EntityMemory get(@NotNull Entity p){
        if(p instanceof Player player) return PlayerMemory.get(player);
        return get(p.getUniqueId());
    }

    static @NotNull EntityMemory getOrCreate(@NotNull Entity p){
        if(p instanceof Player player) return PlayerMemory.getOrCreate(player);
        return getOrCreate(p, null);
    }

    /**
     * @param newConsumer The consumer to use if the memory object needs to be created.
     *                    The memory gets accepted before it is registered.
     */
    static @NotNull EntityMemory getOrCreate(@NotNull Entity p, @Nullable Consumer<EntityMemory> newConsumer){
        if(p instanceof Player player){
            return PlayerMemory.getOrCreate(player, newConsumer == null ? null : newConsumer::accept);
        }
        EntityMemory d = get(p.getUniqueId());
        if(d == null){
            SimpleEntityMemory mem = new SimpleEntityMemory(p);
            if(newConsumer != null) newConsumer.accept(mem);
            d = register(mem);
        }
        return d;
    }

    static <T extends EntityMemory> T register(@NotNull T tick){
        for(Map.Entry<Plugin, Set<Consumer<EntityMemory>>> entry : ADD_FUNCTIONS.entrySet()){
            for(Consumer<EntityMemory> d : entry.getValue()){
                d.accept(tick);
            }
        }
        if(!tick.isAsync()){
            MAIN_THREAD_REGISTRY.register(tick.getUUID(), tick);
            return tick;
        }
        REGISTRY.register(tick.getUUID(), tick);
        return tick;
    }

    static boolean register(@NotNull EntityMemory tick, boolean override){
        if(!tick.isAsync()){
            if(!override && MAIN_THREAD_REGISTRY.containsKey(tick.getUUID())) return false;
            register(tick);
            return true;
        }

        if(!override && REGISTRY.containsKey(tick.getUUID())) return false;
        register(tick);
        return true;
    }

    static @Nullable EntityMemory get(@NotNull UUID uuid){
        var mem = REGISTRY.get(uuid);
        if(mem != null) return mem;
        return MAIN_THREAD_REGISTRY.get(uuid);
    }

    static @Nullable EntityMemory getMain(@NotNull UUID uuid){
        return MAIN_THREAD_REGISTRY.get(uuid);
    }

    static @Nullable EntityMemory getAsync(@NotNull UUID uuid){
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

    static @Nullable DataHolder getDataHolder(@NotNull UUID uuid, @NotNull Key key){
        EntityMemory mem = get(uuid);
        if(mem == null) return null;
        return mem.getDataHolder(key);
    }

    static <T extends DataHolder> @Nullable T getDataHolder(@NotNull UUID uuid, @NotNull Class<T> clazz, @NotNull Key key){
        EntityMemory mem = get(uuid);
        if(mem == null) return null;
        DataHolder dataHolder = mem.getDataHolder(key);
        if(dataHolder == null) return null;
        if(!clazz.isAssignableFrom(dataHolder.getClass())) return null;
        return clazz.cast(dataHolder);
    }

    static <T extends DataHolder> @Nullable T getOrCreateDataHolder(@NotNull Entity entity, @NotNull Class<T> clazz){
        EntityMemory mem = getOrCreate(entity);
        return mem.getDataHolder(clazz);
    }

    static <T extends DataHolder> T getOrCreateDataHolder(@NotNull Entity entity, @NotNull Class<T> clazz, @NotNull Function<EntityMemory, T> function){
        EntityMemory mem = getOrCreate(entity);
        return mem.getDataHolderOrCompute(clazz, function);
    }

    Map<UUID, CompletableFuture<Void>> REMOVING_FUTURES = new HashMap<>();
    ExecutorService CLEANUP_EXECUTOR = Executors.newFixedThreadPool(4);

    @NotNull KeyedRegistry<DataHolder> getDataHolders();
    @Nullable DataHolder getDataHolder(@NotNull Key key);

    default boolean isAsync(){
        return true;
    }

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

    default <T extends DataHolder> T getDataHolderOrCompute(@NotNull Class<T> clazz, @NotNull Function<EntityMemory, T> function){
        T holder = getDataHolder(clazz);
        if(holder==null){
            holder = function.apply(this);
            if(holder != null) getDataHolders().register(holder);
        }
        return holder;
    }

    @NotNull UUID getUUID();

    /**
     * @return Whether to remove this from memory.
     */
    boolean tick();

    void forceRemoveDataHolders(@Nullable Entity e);
    void removeDataHolders(@Nullable Entity e);

    /**
     * This is called immediately when this EntityMemory is supposed to be
     * removed from memory (e.x., when a player quits the server or when an entity is removed from world)
     */
    void onMemoryUnload(@NotNull Entity e);
}
