package killercreepr.crux.core.util;

import net.minecraft.util.Mth;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CruxEntityUtil {
    private static final @NotNull EquipmentSlot[] GENERAL_SLOTS = new EquipmentSlot[]{
        EquipmentSlot.HEAD,
        EquipmentSlot.CHEST,
        EquipmentSlot.LEGS,
        EquipmentSlot.FEET
    };
    public static @NotNull EquipmentSlot[] getGeneralEntitySlots(){
        return GENERAL_SLOTS;
    }

    public static boolean isNonSurvival(Entity e){
        if(!(e instanceof HumanEntity p)) return false;
        GameMode mode = p.getGameMode();
        return mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR;
    }

    public static boolean isValid(Entity e){
        if(e == null) return false;
        if(!e.isValid()) return false;
        if(e instanceof Player p) return p.isOnline();
        return true;
    }

    /*public static boolean isRealisticallyVisible(Entity target){
        if(target == null) return false;
        if(target.isInvisible()) return false;
        LivingEntity le;
        if(target instanceof LivingEntity d) le = d;
        else le = null;

        if(le != null){
            if(le.hasPotionEffect(PotionEffectType.GLOWING)) return true;
        }
        return le == null || !le.hasPotionEffect(PotionEffectType.INVISIBILITY);
    }*/

    public static boolean isRealisticallyVisibleTo(Entity e, Entity target){
        if(e instanceof Player p){
            if(!p.canSee(target)) return false;
        }
        if(target == null) return false;
        if(target.isInvisible()) return false;
        LivingEntity le;
        if(target instanceof LivingEntity d) le = d;
        else le = null;

        if(le != null){
            if(le.hasPotionEffect(PotionEffectType.GLOWING)) return true;
        }
        return le == null || !le.hasPotionEffect(PotionEffectType.INVISIBILITY);
    }

    public static Vector calculateViewVector(float pitch, float yaw) {
        float f2 = pitch * 0.017453292F;
        float f3 = -yaw * 0.017453292F;
        float f4 = Mth.cos(f3);
        float f5 = Mth.sin(f3);
        float f6 = Mth.cos(f2);
        float f7 = Mth.sin(f2);

        return new Vector((f5 * f6), (-f7), (f4 * f6));
    }

    public static boolean isBlocking(@Nullable Entity e){
        if(!(e instanceof HumanEntity living)) return false;
        return living.isBlocking();
    }

    public static boolean isDamageSourceBlocked(@NotNull Entity entity, @NotNull Entity damager) {
        Vector vec3d = damager.getLocation().toVector();
        return isDamageSourceBlocked(entity, vec3d);
    }

    public static boolean isDamageSourceBlocked(@NotNull Entity entity, @NotNull Vector hit) {
        Vector vec3d1 = CruxEntityUtil.calculateViewVector(0.0F, entity.getYaw());
        Vector pos = entity.getLocation().toVector();
        Vector vec3d2 = new Vector(pos.getX() - hit.getX(), pos.getY() - hit.getY(), pos.getZ() - hit.getZ());

        vec3d2 = (new Vector(vec3d2.getX(), 0.0D, vec3d2.getZ())).normalize();
        return vec3d2.dot(vec3d1) < 0.0D;
    }

    public static @NotNull Location getEyeOrLocation(@NotNull Entity e){
        if(e instanceof LivingEntity d) return d.getEyeLocation();
        return e.getLocation();
    }

    public static @Nullable Block getBlockStandingOn(@NotNull Entity entity) {
        Block block = entity.getLocation().getBlock();
        Block blockBelow = block.getRelative(BlockFace.DOWN);
        if (!block.getType().isEmpty() && block.getType() != Material.LIGHT) return block;
        if (!blockBelow.getType().isEmpty()) return blockBelow;

        // Expand players hitbox by 0.3, which is the maximum size a player can be off a block
        // Whilst not falling off
        BoundingBox entityBox = entity.getBoundingBox().expand(0.3);
        for (BlockFace face : BlockFace.values()) {
            if (!face.isCartesian() || face.getModY() != 0) continue;
            Block relative = blockBelow.getRelative(face);
            if (relative.getType() == Material.AIR) continue;
            if (relative.getBoundingBox().overlaps(entityBox)) return relative;
        }

        return null;
    }

    public static void giveOrDrop(@NotNull HumanEntity p, @NotNull Collection<ItemStack> items){
        giveOrDrop(p, null, items);
    }

    public static void giveOrDrop(@NotNull HumanEntity p, @Nullable Consumer<Item> spawnConsumer, @NotNull Collection<ItemStack> items){
        giveOrDrop(p, spawnConsumer, items.toArray(new ItemStack[0]));
    }

    public static void giveOrDrop(@NotNull HumanEntity p, @NotNull ItemStack... items){
        giveOrDrop(p, null, items);
    }

    public static void giveOrDrop(@NotNull HumanEntity p, @Nullable Consumer<Item> spawnConsumer, @NotNull ItemStack... items){
        for(ItemStack drop : p.getInventory().addItem(items).values()){
            p.getWorld().dropItem(p.getLocation(), drop, spawnConsumer);
        }
    }

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

    public static @NotNull <T extends Entity> List<T> filterEntityDistance(@NotNull List<T> list, @NotNull Location loc, @Nullable Integer amount, boolean farthest){
        HashMap<T, Float> closest = new HashMap<>();
        for(T p : list){
            closest.put(p, (float) p.getLocation().distance(loc));
        }

        List<Map.Entry<T, Float>> sortedList = CruxMap.sortKeysByFloat(closest);
        int index = farthest ? sortedList.size() : -1;

        List<T> playerList = new ArrayList<>();
        if(amount == null || amount < 0) amount = list.size();
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

    public static @NotNull Collection<Entity> getEntitiesNearChunk(@NotNull Chunk chunk, int radius, @Nullable Predicate<Entity> predicate){
        Collection<Entity> list = new HashSet<>();
        for(int x = -radius; x < radius; x++){
            for(int z = -radius; z < radius; z++){
                if(!chunk.getWorld().isChunkLoaded(chunk.getX()+x, chunk.getZ()+z)) continue;
                Chunk c = chunk.getWorld().getChunkAt(chunk.getX()+x, chunk.getZ()+z);
                if(predicate == null) list.addAll(List.of(c.getEntities()));
                else{
                    for(Entity e : c.getEntities()){
                        if(predicate.test(e)) list.add(e);
                    }
                }
            }
        }
        return list;
    }

    public static boolean checkNearbyEntityChunkAmount(@NotNull Chunk chunk, int radius, int amount, @Nullable Predicate<Entity> predicate){
        int entityAmount = 0;
        for(int x = -radius; x < radius; x++){
            for(int z = -radius; z < radius; z++){
                if(!chunk.getWorld().isChunkLoaded(chunk.getX()+x, chunk.getZ()+z)) continue;
                Chunk c = chunk.getWorld().getChunkAt(chunk.getX()+x, chunk.getZ()+z);
                if(predicate == null){
                    amount += c.getEntities().length;
                    entityAmount++;
                    if(entityAmount >= amount) return true;
                } else{
                    for(Entity e : c.getEntities()){
                        if(predicate.test(e)){
                            amount++;
                            entityAmount++;
                            if(entityAmount >= amount) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static int getEntityAmountNearChunk(@NotNull Chunk chunk, int radius, @Nullable Predicate<Entity> predicate){
        int amount = 0;
        for(int x = -radius; x < radius; x++){
            for(int z = -radius; z < radius; z++){
                if(!chunk.getWorld().isChunkLoaded(chunk.getX()+x, chunk.getZ()+z)) continue;
                Chunk c = chunk.getWorld().getChunkAt(chunk.getX()+x, chunk.getZ()+z);
                if(predicate == null) amount += c.getEntities().length;
                else{
                    for(Entity e : c.getEntities()){
                        if(predicate.test(e)) amount++;
                    }
                }
            }
        }
        return amount;
    }
}
