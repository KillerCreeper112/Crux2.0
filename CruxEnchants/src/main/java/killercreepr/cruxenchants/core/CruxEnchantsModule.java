package killercreepr.cruxenchants.core;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.cruxenchants.api.enchant.CruxEnchantManager;
import killercreepr.cruxenchants.core.enchant.SimpleEnchantManager;
import killercreepr.cruxenchants.core.registries.CruxEnchantRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public class CruxEnchantsModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_ENCHANTS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected final @NotNull CruxEnchantManager enchantManager = new SimpleEnchantManager(CruxEnchantRegistries.ENCHANT_WRAPPER);

    public @NotNull CruxEnchantManager enchantManager() {
        return enchantManager;
    }

    public void bootstrap(@NotNull BootstrapContext context) {
        // Register a new handled for the freeze lifecycle event on the enchantment registry
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            EnchantmentRegistryEntry. Builder d;
            event.registry().register(
                // The key of the registry
                // Plugins should use their own namespace instead of minecraft or papermc
                TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("papermc:pointy")),
                b -> b.description(Component.text("Pointy"))
                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
                    .anvilCost(1)
                    .maxLevel(25)
                    .weight(10)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                    .activeSlots(EquipmentSlotGroup.ANY)
            );
        }));
    }
}
