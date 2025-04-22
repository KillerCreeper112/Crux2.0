package killercreepr.cruxpotions.core.item;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.Crux;
import killercreepr.cruxitems.api.item.CruxedItemUpdater;
import killercreepr.cruxitems.core.item.CruxedItemUpdateContext;
import killercreepr.cruxpotions.core.component.PotionComponents;
import killercreepr.cruxpotions.core.persistence.PotionPersistTags;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PotionItemUpdater implements CruxedItemUpdater {
    public static final Key KEY = Crux.key("crux_potions");
    @Override
    public void onUpdate(@NotNull CruxedItemUpdateContext ctx) {
        CruxItem crux = ctx.getItem();
        ItemStack item = crux.item();
        if(!(item.getItemMeta() instanceof PotionMeta meta)) return;

        Collection<StoredPotion> storedPotions = CruxItem.wrap(item).getOrDefault(PotionComponents.STORED_CRUX_POTIONS, List.of());
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
