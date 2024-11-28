package killercreepr.cruxenchants.core.util;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.event.WritableRegistry;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.item.functions.ItemEnchantFunction;
import killercreepr.cruxenchants.api.enchant.CruxEnchantWrapper;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EnchantRegistererUtil {
    protected final WritableRegistry<Enchantment, EnchantmentRegistryEntry.Builder> registry;
    protected final KeyedRegistry<CruxEnchantWrapper> wrapperRegistry;
    protected final RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event;
    public EnchantRegistererUtil(WritableRegistry<Enchantment, EnchantmentRegistryEntry.Builder> registry,
                                 @Nullable KeyedRegistry<CruxEnchantWrapper> wrapperRegistry, RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event) {
        this.registry = registry;
        this.wrapperRegistry = wrapperRegistry;
        this.event = event;
    }

    public void register(){
    }

    public void register(TypedKey<Enchantment> typedKey, Consumer<EnchantmentRegistryEntry.Builder> consumer){
        registry.register(typedKey, consumer);
        if(wrapperRegistry == null) return;
        wrapperRegistry.register(CruxEnchantWrapper.enchantWrapper(typedKey.key()));
    }

    public void register(Key key, Consumer<EnchantmentRegistryEntry.Builder> consumer){
        register(TypedKey.create(RegistryKey.ENCHANTMENT, key), consumer);
    }

    public void register(String key, Consumer<EnchantmentRegistryEntry.Builder> consumer){
        register(Crux.key(key), consumer);
    }

    public void register(TypedKey<Enchantment> typedKey, Consumer<EnchantmentRegistryEntry.Builder> consumer,
                         CruxEnchantWrapper wrapper){
        registry.register(typedKey, consumer);
        if(wrapperRegistry == null) return;
        wrapperRegistry.register(wrapper);
    }
}
