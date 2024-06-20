package killercreepr.cruxpotions.tags;

import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.StringListTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxpotions.persistence.PotionPersistTags;
import killercreepr.cruxpotions.persistence.StoredPotion;
import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.CruxPotion;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotionsLoreTag implements ObjectTag<ItemStack> {
    protected final @NotNull Holder<List<String>> potionsFormat;
    public PotionsLoreTag(@NotNull Holder<List<String>> potionsFormat) {
        this.potionsFormat = potionsFormat;
    }

    public @NotNull Holder<List<String>> getPotionsFormat() {
        return potionsFormat;
    }

    @Override
    public @NotNull Class<ItemStack> getObjectType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("cruxpotions_");
    }

    @Override
    public @Nullable StringListTagContainer requestStringLists(@NotNull ItemStack item, @NotNull TagParser tags) {
        return new StringListTagContainer(tags)
            .add(Tag.stringList("potions", (args, context) ->{
                List<String> format = potionsFormat.value();
                if(format==null) return null;

                Collection<StoredPotion> storedPotions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(item, null);
                if(storedPotions==null) return List.of();
                List<String> list = new ArrayList<>();
                storedPotions.forEach(potion ->{
                    CruxPotion crux = potion.getPotion();
                    String formatted = ActivePotion.formatPotion(potion);

                    String color = switch (crux.getCategory()){
                        case HARMFUL -> "<red>";
                        default -> "<blue>";
                    };

                    for(String s : format){
                        list.add(
                            s.replace("<potion_name>", crux.getName())
                                .replace("<potion_key>", crux.key().asString())
                                .replace("<potion_duration>", potion.getDuration() + "")
                                .replace("<potion_amplifier>", potion.getAmplifier() + "")
                                .replace("<potion_formatted>", formatted)
                                .replace("<potion_duration_formatted>", ActivePotion.formatDuration(potion.getDuration()))
                                .replace("<potion_category_color>", color)
                        );
                    }
                });
                return list;
            }))
            ;
    }
}
