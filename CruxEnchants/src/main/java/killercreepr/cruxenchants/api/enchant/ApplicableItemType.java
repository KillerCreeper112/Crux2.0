package killercreepr.cruxenchants.api.enchant;

import killercreepr.crux.core.Crux;
import killercreepr.cruxenchants.core.enchant.SimpleApplicableItemType;
import killercreepr.cruxenchants.core.registries.CruxEnchantRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface ApplicableItemType extends Keyed {
    static ApplicableItemType itemType(Key key, String symbol){
        return new SimpleApplicableItemType(key, symbol);
    }

    ApplicableItemType SWORD = register(itemType(Crux.key("sword"), ""));
    ApplicableItemType PICKAXE = register(itemType(Crux.key("pickaxe"), ""));
    ApplicableItemType AXE = register(itemType(Crux.key("axe"), ""));
    ApplicableItemType SHOVEL = register(itemType(Crux.key("shovel"), ""));
    ApplicableItemType HOE = register(itemType(Crux.key("hoe"), ""));
    ApplicableItemType MACE = register(itemType(Crux.key("mace"), ""));
    ApplicableItemType TRIDENT = register(itemType(Crux.key("trident"), ""));

    ApplicableItemType HELMET = register(itemType(Crux.key("helmet"), ""));
    ApplicableItemType CHESTPLATE = register(itemType(Crux.key("chestplate"), ""));
    ApplicableItemType LEGGINGS = register(itemType(Crux.key("leggings"), ""));
    ApplicableItemType BOOTS = register(itemType(Crux.key("boots"), ""));

    private static ApplicableItemType register(ApplicableItemType t){
        return CruxEnchantRegistries.APPLICABLE_ITEM_TYPE.register(t);
    }

    @NotNull String symbol();
}
