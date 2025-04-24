package killercreepr.cruxenchants.api.enchant;

import killercreepr.crux.core.Crux;
import killercreepr.cruxenchants.core.enchant.SimpleApplicableItemType;
import killercreepr.cruxenchants.core.registries.CruxEnchantRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ApplicableItemType extends Keyed {
    static ApplicableItemType itemType(Key key, String symbol){
        return new SimpleApplicableItemType(key, symbol);
    }

    List<String> PRIORITY_ORDER = List.of(
        "sword", "axe", "mace", "trident", "bow", "crossbow",
        "pickaxe", "shovel", "hoe", "fishing_rod",
        "helmet", "chestplate", "leggings", "boots"
    );

    ApplicableItemType SWORD = register(itemType(Crux.key("sword"), "4"));
    ApplicableItemType PICKAXE = register(itemType(Crux.key("pickaxe"), "0"));
    ApplicableItemType AXE = register(itemType(Crux.key("axe"), "1"));
    ApplicableItemType SHOVEL = register(itemType(Crux.key("shovel"), "3"));
    ApplicableItemType HOE = register(itemType(Crux.key("hoe"), "2"));
    ApplicableItemType MACE = register(itemType(Crux.key("mace"), "c"));
    ApplicableItemType TRIDENT = register(itemType(Crux.key("trident"), "b"));
    ApplicableItemType BOW = register(itemType(Crux.key("bow"), "9"));
    ApplicableItemType CROSSBOW = register(itemType(Crux.key("crossbow"), "a"));

    ApplicableItemType FISHING_ROD = register(itemType(Crux.key("fishing_rod"), "d"));

    ApplicableItemType HELMET = register(itemType(Crux.key("helmet"), "5"));
    ApplicableItemType CHESTPLATE = register(itemType(Crux.key("chestplate"), "6"));
    ApplicableItemType LEGGINGS = register(itemType(Crux.key("leggings"), "7"));
    ApplicableItemType BOOTS = register(itemType(Crux.key("boots"), "8"));

    private static ApplicableItemType register(ApplicableItemType t){
        return CruxEnchantRegistries.APPLICABLE_ITEM_TYPE.register(t);
    }

    @NotNull String symbol();
}
