package killercreepr.cruxattributes.core.loot.functions;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.impl.functions.SimpleLootFunction;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxSlot;
import killercreepr.cruxattributes.core.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

public class ItemCruxAttributeFunction extends SimpleLootFunction<ItemStack> {
    private final @NotNull Map<Key, Collection<Modifier>> modifiers;

    public ItemCruxAttributeFunction(@Nullable Collection<LootCondition> conditions, @NotNull Map<Key, Collection<Modifier>> modifiers) {
        super(conditions);
        this.modifiers = modifiers;
    }
    public ItemCruxAttributeFunction(@NotNull Map<Key, Collection<Modifier>> modifiers) {
        this(null, modifiers);
    }

    public CruxAttribute getAttribute(@NotNull Key key){
        return CruxAttributeRegistries.ATTRIBUTES.get(key);
    }

    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        Random random = context.getRandom();
        for(Map.Entry<Key, Collection<Modifier>> entry : modifiers.entrySet()){
            CruxAttribute attribute = getAttribute(entry.getKey());
            if(attribute==null) throw new IllegalStateException(entry.getKey() + " crux attribute not found!");
            Collection<CruxAttributeModifier> itemModifiers = CruxAttribute.getModifiers(i, attribute);
            for(Modifier m : entry.getValue()){
                double value = 0D;
                for(CruxAttributeModifier itemMod : itemModifiers){
                    if(itemMod.key().equals(m.key()) && itemMod.getOperation().equals(m.getOperation())){
                        value = itemMod.getAmount();
                        break;
                    }
                }
                value += attribute.round(m.getProvider().sample(random).doubleValue());
                CruxAttribute.addModifier(i, attribute, new CruxAttributeModifier(m.key(), value, m.getOperation(), m.getSlot()));
            }
        }
        return i;
    }

    public final static class Modifier implements Keyed {
        private final Key key;
        private final NumberProvider provider;
        private final CruxAttribute.Operation operation;
        private final CruxSlot slot;

        public Modifier(@NotNull NamespacedKey key, @NotNull NumberProvider provider, @NotNull CruxAttribute.Operation operation) {
            this(key, provider, operation, null);
        }

        public Modifier(@NotNull NamespacedKey key, @NotNull NumberProvider provider, @Nullable CruxSlot slot) {
            this(key, provider, CruxAttribute.Operation.ADD, slot);
        }

        public Modifier(@NotNull NamespacedKey key, @NotNull NumberProvider provider, @NotNull CruxAttribute.Operation operation,
                                     @Nullable CruxSlot slot) {
            this.key = key;
            this.provider = provider;
            this.operation = operation;
            this.slot = slot;
        }
        /**
         * Creates a base attribute modifier.
         */
        public Modifier(@NotNull NumberProvider provider) {
            this(CruxAttribute.k("base"), provider, CruxAttribute.Operation.ADD);
        }

        /**
         * Creates a base attribute modifier.
         */
        public Modifier(@NotNull NumberProvider provider, @Nullable CruxSlot slot) {
            this(CruxAttribute.k("base"), provider, CruxAttribute.Operation.ADD, slot);
        }

        public boolean isBase(){
            return key.equals(CruxAttribute.k("base"));
        }

        @Override
        public @NotNull Key key() {
            return key;
        }

        public @NotNull NumberProvider getProvider() {
            return provider;
        }

        public @NotNull CruxAttribute.Operation getOperation() {
            return operation;
        }

        @Nullable
        public CruxSlot getSlot() {
            return slot;
        }
    }
}
