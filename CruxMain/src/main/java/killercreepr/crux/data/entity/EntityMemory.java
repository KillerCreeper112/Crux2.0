package killercreepr.crux.data.entity;

import killercreepr.crux.data.Holder;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

public class EntityMemory implements Holder<Entity> {
    public static final MappedRegistry<Plugin, Set<Consumer<EntityMemory>>> ADD_FUNCTIONS = new SimpleMappedRegistry<>();
    public static @NotNull Consumer<EntityMemory> registerFunction(@NotNull Plugin plugin, @NotNull Consumer<EntityMemory> function){
        Set<Consumer<EntityMemory>> data = ADD_FUNCTIONS.getOrDefault(plugin, new HashSet<>());
        data.add(function);
        ADD_FUNCTIONS.register(plugin, data);
        return function;
    }

    public static boolean removeFunction(@NotNull Plugin plugin, @NotNull Consumer<EntityMemory> function){
        Set<Consumer<EntityMemory>> data = ADD_FUNCTIONS.getOrDefault(plugin, new HashSet<>());
        boolean b = data.remove(function);
        if(data.isEmpty()) ADD_FUNCTIONS.remove(plugin);
        else ADD_FUNCTIONS.register(plugin, data);
        return b;
    }

    public static @NotNull MappedRegistry<Plugin, Set<Consumer<EntityMemory>>> getAddFunctions(){ return ADD_FUNCTIONS; }

    public static final EntityMemoryRegistry<EntityMemory> REGISTRY = new EntityMemoryRegistry<>();

    public static <T extends DataHolder> @NotNull Collection<T> getAllDataHolders(@NotNull Class<T> clazz){
        Collection<T> list = new HashSet<>();
        for(EntityMemory data : REGISTRY){
            T holder = data.getHolder(clazz);
            if(holder == null) continue;
            list.add(holder);
        }
        return list;
    }

    public static @Nullable EntityMemory get(@NotNull Entity p){
        if(p instanceof Player player) return PlayerMemory.get(player);
        return get(p.getUniqueId());
    }

    public static @NotNull EntityMemory getOrCreate(@NotNull Entity p){
        if(p instanceof Player player) return PlayerMemory.getOrCreate(player);
        EntityMemory d = get(p.getUniqueId());
        if(d == null){
            d = register(new EntityMemory(p));
        }
        return d;
    }
    public static <T extends EntityMemory> T register(@NotNull T tick){
        for(Map.Entry<Plugin, Set<Consumer<EntityMemory>>> entry : ADD_FUNCTIONS.entrySet()){
            for(Consumer<EntityMemory> d : entry.getValue()){
                d.accept(tick);
            }
        }
        REGISTRY.register(tick.getUUID(), tick);
        return tick;
    }

    public static boolean register(@NotNull EntityMemory tick, boolean override){
        if(!override && REGISTRY.containsKey(tick.getUUID())) return false;
        register(tick);
        return true;
    }

    public static @Nullable EntityMemory get(@NotNull UUID uuid){
        return REGISTRY.getOrDefault(uuid, null);
    }

    public static <T extends DataHolder> @Nullable T getDataHolder(@NotNull Entity entity, @NotNull Class<T> clazz){
        return getDataHolder(entity.getUniqueId(), clazz);
    }

    public static <T extends DataHolder> @Nullable T getDataHolder(@NotNull UUID uuid, @NotNull Class<T> clazz){
        EntityMemory mem = get(uuid);
        if(mem == null) return null;
        return mem.getHolder(clazz);
    }

    public static <T extends DataHolder> @Nullable T getOrCreateDataHolder(@NotNull Entity entity, @NotNull Class<T> clazz){
        EntityMemory mem = getOrCreate(entity);
        return mem.getHolder(clazz);
    }

    protected final KeyedRegistry<DataHolder> holders = new SimpleKeyedRegistry<>();
    protected final UUID uuid;
    protected final Holder<? extends Entity> entity;
    public EntityMemory(@NotNull Entity e) {
        this(e.getUniqueId(), Holder.weakReference(e));
    }

    public EntityMemory(@NotNull UUID uuid, @NotNull Holder<? extends Entity> holder){
        this.uuid = uuid;
        this.entity = holder;
    }

    public @NotNull KeyedRegistry<DataHolder> getHolders() {
        return holders;
    }
    public boolean contains(@NotNull DataHolder holder){
        return contains(holder.key());
    }

    public boolean contains(@NotNull Key holder){
        return holders.containsKey(holder);
    }

    public @Nullable DataHolder getHolder(@NotNull Key key){
        return holders.get(key);
    }

    public <T extends DataHolder> @Nullable T getHolder(@NotNull Class<T> clazz){
        try{
            Field staticKeyField = clazz.getDeclaredField("KEY");
            Key key = (Key) staticKeyField.get(null);
            try{
                return clazz.cast(getHolder(key));
            }catch (NullPointerException | ClassCastException ignored){ return null; }
        }catch (NoSuchFieldException | IllegalAccessException ignored){ return null; }
    }


    public <T extends DataHolder> @NotNull T getHolderOrThrow(@NotNull Class<T> clazz){
        T value = getHolder(clazz);
        if(value == null) throw new RuntimeException("EntityMemory does not have DataHolder specified! " + clazz);
        return value;
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }

    /**
     * @return Whether to remove this from memory.
     */
    public boolean tick(){
        Entity e = value();
        holders.values().removeIf(x ->{
            if(x.shouldRemoveFromMemory(e)){
                x.removing(e);
                return true;
            }
            if(e != null) x.tick(e);
            return false;
        });
        if(shouldRemoveFromMemory(e)){
            removeDataHolders(e, true);
            return true;
        }
        return false;
    }

    public void removeDataHolders(@Nullable Entity e, boolean parent){
        for(DataHolder h : holders.values()){
            if(parent) h.parentRemoving(e);
            else h.removing(e);
        }
    }

    protected boolean shouldRemoveFromMemory(@Nullable Entity e){
        return e == null || !e.isValid() || holders.isEmpty();
    }

    @Nullable
    @Override
    public Entity value() {
        return entity.value();
    }
}
