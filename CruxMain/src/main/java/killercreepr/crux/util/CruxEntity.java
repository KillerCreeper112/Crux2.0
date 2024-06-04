package killercreepr.crux.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class CruxEntity {
    public static @Nullable Player getPlayer(@NotNull String uuidOrName){
        if(uuidOrName.isBlank()) return null;
        Player p = Bukkit.getPlayer(uuidOrName);
        if(p == null){
            try{
                return Bukkit.getPlayer(UUID.fromString(uuidOrName));
            }catch (IllegalArgumentException ignored){}
        }
        return p;
    }

    public static @Nullable Entity getEntity(@NotNull String uuidOrName){
        if(uuidOrName.isBlank()) return null;
        Entity e = Bukkit.getPlayer(uuidOrName);
        if(e == null){
            try{
                return Bukkit.getEntity(UUID.fromString(uuidOrName));
            }catch (IllegalArgumentException ignored){}
        }
        return e;
    }

    public static @NotNull <T extends Entity> List<T> filterEntityDistance(@NotNull List<T> list, @NotNull Location loc, boolean farthest){
        return filterEntityDistance(list, loc, -1, farthest);
    }

    public static @NotNull <T extends Entity> List<T> filterEntityDistance(@NotNull List<T> list, @NotNull Location loc, int amount, boolean farthest){
        HashMap<T, Float> closest = new HashMap<>();
        for(T p : list){
            closest.put(p, (float) p.getLocation().distance(loc));
        }

        List<Map.Entry<T, Float>> sortedList = CruxMap.sortMapByFloat(closest);
        int index = farthest ? sortedList.size() : -1;

        List<T> playerList = new ArrayList<>();
        if(amount < 0) amount = list.size();
        for(; amount > 0; amount--){
            index += farthest ? -1 : 1;
            if(index >= sortedList.size() || index < 0) break;
            playerList.add(sortedList.get(index).getKey());
        }
        return playerList;
    }

    public static <T extends Entity> @NotNull List<T> getEntitiesNear(@NotNull Class<T> type, @NotNull Location location, double radius, @Nullable Predicate<T> filter){
        List<T> list = new ArrayList<>();
        for(Entity e : getEntitiesNear(location, radius, null)){
            if(!type.isAssignableFrom(e.getClass())) continue;
            T parsed = type.cast(e);
            if(filter != null && !filter.test(parsed)) continue;
            list.add(parsed);
        }
        return list;
    }

    public static @NotNull List<Entity> getEntitiesNear(@NotNull Location location, double radius, @Nullable Predicate<Entity> filter) {
        List<Entity> entities = new ArrayList<>();
        World world = location.getWorld();

        // Find chunks
        int smallX = (int) Math.floor((location.getX() - radius) / 16.0D);
        int bigX = (int) Math.floor((location.getX() + radius) / 16.0D);
        int smallZ = (int) Math.floor((location.getZ() - radius) / 16.0D);
        int bigZ = (int) Math.floor((location.getZ() + radius) / 16.0D);

        for (int x = smallX; x <= bigX; x++) {
            for (int z = smallZ; z <= bigZ; z++) {
                if (world.isChunkLoaded(x, z)) {
                    entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities())); // Add all entities from this chunk to the list
                }
            }
        }

        // Remove the entities that are within the box above but not in sphere radius.
        entities.removeIf(entity ->{
            if(filter != null && !filter.test(entity)) return true;
            return (entity.getLocation().distanceSquared(location) > radius * radius);
        });
        return entities;
    }
}
