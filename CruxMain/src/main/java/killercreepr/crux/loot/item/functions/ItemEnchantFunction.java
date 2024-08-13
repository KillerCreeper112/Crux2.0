package killercreepr.crux.loot.item.functions;

import killercreepr.crux.loot.SimpleLootObject;
import killercreepr.crux.loot.SimpleLootTable;
import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.crux.loot.functions.SimpleLootFunction;
import killercreepr.crux.valueproviders.number.NumberProvider;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ItemEnchantFunction extends SimpleLootFunction<ItemStack> {
    private final NumberProvider rolls;
    private final Collection<Enchant> enchants;

    public ItemEnchantFunction(@NotNull NumberProvider rolls, @NotNull Collection<Enchant> enchants) {
        this(null, rolls, enchants);
    }

    public ItemEnchantFunction(@Nullable Collection<LootCondition> conditions, @NotNull NumberProvider rolls, @NotNull Collection<Enchant> enchants) {
        super(conditions);
        this.rolls = rolls;
        this.enchants = enchants;
    }

    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        Random source = context.getRandom();
        List<Enchant> random = SimpleLootTable.random(enchants, rolls.sample(source).intValue(), context);
        for(Enchant e : random){
            int level = e.getLevelProvider().sample(source).intValue();
            if(level < 1) continue;
            //todo CustomEnchant.set(i, e.getEnchant(), level);
        }
        return i;
    }

    /*public static @Nullable GrimEnchantFunction deserialize(@NotNull JsonElement element){
        if(!element.isJsonObject()) return null;
        JsonObject o = element.getAsJsonObject();
        JsonArray array = o.getAsJsonArray("enchants");
        Collection<Enchant> modifiers = new ArrayList<>();
        array.forEach(x ->{
            Enchant e = Enchant.deserialize(x);
            if(e == null) return;
            modifiers.add(e);
        });
        NumberProvider rolls;
        JsonElement e = o.get("rolls");
        rolls = e == null ? new ConstantNumber(modifiers.size()) : NumberProvider.deserialize(e);
        return new GrimEnchantFunction(deserializeConditions(o), rolls, modifiers);
    }

    @Override
    public @NotNull JsonObject serialize() {
        JsonObject complete = new JsonObject();
        JsonArray array = new JsonArray();
        for(Enchant e : enchants){
            array.add(e.serialize());
        }
        complete.addProperty("function", getType().toString().toLowerCase());
        complete.add("rolls", rolls.serialize());
        complete.add("enchants", array);
        complete.add("conditions", serializeConditions());
        return complete;
    }*/

    public final static class Enchant extends SimpleLootObject<ItemStack> {
        private final Key enchant;
        private final NumberProvider levelProvider;
        public Enchant(@NotNull Key enchant, @NotNull NumberProvider levelProvider, @NotNull SimpleLootObject<ItemStack> object) {
            super(object);
            this.enchant = enchant;
            this.levelProvider = levelProvider;
        }

        /*public static @Nullable Enchant deserialize(@NotNull JsonElement e){
            GrimLootObject lootObject = GrimLootObject.deserialize(e);
            if(lootObject == null) return null;
            JsonObject o = e.getAsJsonObject();
            CustomEnchant enchant = CustomEnchant.get(DP.key(o.get("enchant").getAsString()));
            if(enchant == null) return null;
            return new Enchant(enchant, NumberProvider.deserialize(o.get("level")), lootObject);
        }

        @Override
        public @NotNull JsonElement serialize(){
            JsonObject o = (JsonObject) super.serialize();
            o.addProperty("enchant", enchant.getKey().asString());
            o.add("level", levelProvider.serialize());
            return o;
        }*/

        public @NotNull Key getEnchant() {
            return enchant;
        }

        public @NotNull NumberProvider getLevelProvider() {
            return levelProvider;
        }
    }
}
