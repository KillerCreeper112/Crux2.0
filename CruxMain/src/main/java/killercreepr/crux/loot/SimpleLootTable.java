package killercreepr.crux.loot;

import killercreepr.crux.loot.api.*;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.valueproviders.number.NumberProvider;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class SimpleLootTable<T> implements LootTable<T> {
    private final Key key;
    private final NumberProvider rolls;
    private final List<LootPool<T>> pools;
    public SimpleLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<T>> pools) {
        this.key = key;
        this.rolls = rolls;
        this.pools = pools;
    }

    public @NotNull NumberProvider getRolls() {
        return rolls;
    }

    /*public static @Nullable GrimLootTable deserialize(@NotNull NamespacedKey key, @NotNull JsonObject o){
        JsonElement pool = o.get("pools");
        if(pool == null || !pool.isJsonArray()) return null;
        JsonElement r = o.get("rolls");
        if(r == null || !r.isJsonObject()) return null;
        JsonObject rO = r.getAsJsonObject();
        NumberProvider rolls;
        if(rO.get("type") == null) rolls = new ConstantNumber(rO.get("value").getAsInt());
        else{
            String s = rO.get("type").getAsString();
            if (s.equalsIgnoreCase("uniform")) {
                rolls = new UniformNumber(rO.get("min").getAsInt(), rO.get("max").getAsInt());
            } else rolls = new ConstantNumber(rO.get("value").getAsInt());
        }
        List<GrimLootPool> pools = new ArrayList<>();
        JsonArray poolArray = pool.getAsJsonArray();
        poolArray.forEach(e ->{
            GrimLootPool p = GrimLootPool.deserialize(e);
            if(p == null) return;
            pools.add(p);
        });
        if(pools.isEmpty()){
            Crux.log(Level.WARNING, "Pool list is empty for " + key);
            return null;
        }
        return new GrimLootTable(key, rolls, pools);
    }*/

    /*public @NotNull JsonElement serialize(){
        JsonObject o = new JsonObject();
        JsonArray a = new JsonArray();
        for(GrimLootPool p : pools){
            a.add(p.serialize());
        }
        o.add("rolls", rolls.serialize());
        o.add("pools", a);
        return o;
    }*/

    /*public @NotNull JsonElement serialize(@NotNull JsonObject o){
        JsonArray a = new JsonArray();
        for(GrimLootPool p : pools){
            a.add(p.serialize());
        }
        o.add("rolls", rolls.serialize());
        o.add("pools", a);
        return o;
    }*/

    public @NotNull List<LootPool<T>> getPools() {
        return pools;
    }

    @Override
    public @NotNull Collection<T> populateLoot(@NotNull LootContext context) {
        return populateLoot(context, null, false);
    }

    @Override
    public @NotNull Collection<T> populateLoot(@NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude, boolean excludeEmpty) {
        List<T> list = new ArrayList<>();
        List<LootPool<T>> data = exclude == null ? this.pools : new ArrayList<>(this.pools);
        if(excludeEmpty && exclude != null) data.removeIf(x -> x.isEmptyWith(exclude));
        for(LootPool<T> pool : random(rolls.sample(context.getRandom()).intValue(), context)){
            list.addAll(pool.populateLoot(context));
        }
        return list;
    }

    //@Override
    /*public void fillInventory(@NotNull Inventory inventory, @NotNull LootContext context) {
        Random random = new Random();
        for(ItemStack i : populateLoot(context)){
            int index = random.nextInt(0, inventory.getSize());
            ItemStack empty = inventory.getItem(index);
            while(!CruxItem.isEmpty(empty)){
                index++;
                if(index >= inventory.getSize()) return;
                empty = inventory.getItem(index);
            }
            inventory.setItem(index, i);
        }
    }*/

    public static <T extends LootObject<?>> @NotNull List<T> random(@NotNull Collection<T> poll, int rolls, @NotNull LootContext context){
        float entityChance = 0f;

        if(context.getLooter() instanceof LivingEntity e){
            if(e.hasPotionEffect(PotionEffectType.LUCK)){
                entityChance *= (1f + (.1f * (e.getPotionEffect(PotionEffectType.LUCK).getAmplifier() + 1)));
            }
            if(e.hasPotionEffect(PotionEffectType.UNLUCK)){
                entityChance *= (1f - (.1f * (e.getPotionEffect(PotionEffectType.UNLUCK).getAmplifier() + 1)));
            }
        }
        entityChance /= 4f;

        List<T> list = new ArrayList<>();
        LinkedHashMap<T, Integer> data = new LinkedHashMap<>();
        for(T p : poll){
            data.put(p, p.getWeight());
        }
        for(int i = 0; i < rolls; i++){
            int totalWeight = 0;
            int weight;
            float quality;
            for(T item : new HashSet<>(data.keySet())){
                boolean tested = true;
                for(LootCondition c : item.getConditions()){
                    tested = c.test(context);
                }
                if(!tested){
                    data.remove(item);
                    continue;
                }
                weight = item.getWeight();
                quality = item.getQuality();
                weight += (int) (quality * entityChance);
                if(weight < 1){
                    data.remove(item);
                    continue;
                }
                totalWeight += weight;
                data.put(item, weight);
            }
            int chance = CruxMath.random(0, totalWeight);
            for(Map.Entry<T, Integer> entry : new HashSet<>(data.entrySet())){
                if(chance <= entry.getValue()){
                    data.remove(entry.getKey());
                    list.add(entry.getKey());
                    break;
                }
                chance -= entry.getValue();
            }
        }
        return list;
    }

    @Override
    public @NotNull List<LootPool<T>> random(int rolls, @NotNull LootContext context){
        return random(pools, rolls, context);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
