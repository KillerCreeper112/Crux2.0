package killercreepr.cruxpotions.item;

import killercreepr.crux.core.Crux;
import killercreepr.cruxitems.item.CruxedItem;
import killercreepr.cruxitems.item.CruxedItemUpdateContext;
import killercreepr.cruxitems.item.CruxedItemUpdater;
import killercreepr.cruxpotions.persistence.PotionPersistTags;
import killercreepr.cruxpotions.persistence.StoredPotion;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class PotionItemUpdater implements CruxedItemUpdater {
    public static final Key KEY = Crux.key("crux_potions");
    @Override
    public void onUpdate(@NotNull CruxedItemUpdateContext ctx) {
        CruxedItem crux = ctx.getItem();
        ItemStack item = crux.item();
        if(!(item.getItemMeta() instanceof PotionMeta meta)) return;

        Collection<StoredPotion> storedPotions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(item, Set.of());
        Color color = null;
        for(StoredPotion pot : storedPotions){
            Color current = pot.getPotion().getColor();
            if(current==null) continue;
            if(color == null){
                color = current;
                continue;
            }
            color = color.mixColors(current);
        }

        if(meta.hasCustomEffects()){
            for(PotionEffect effect : meta.getCustomEffects()){
                Color current = effect.getType().getColor();
                if(color == null){
                    color = current;
                    continue;
                }
                color = color.mixColors(current);
            }
        }
        PotionType type = meta.getBasePotionType();
        if(type != null){
            for(PotionEffect effect : type.getPotionEffects()){
                Color current = effect.getType().getColor();
                if(color == null){
                    color = current;
                    continue;
                }
                color = color.mixColors(current);
            }
        }
        meta.setColor(color);
        item.setItemMeta(meta);
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }
}
