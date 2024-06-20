package killercreepr.cruxenchants.tags;

import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.format.FormatPrefix;
import killercreepr.crux.tags.hook.lore.LoreHook;
import killercreepr.crux.tags.tag.ObjectTag;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxenchants.enchant.CruxEnchant;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EnchantLoreTag extends ObjectTag<ItemStack> {
    protected final @NotNull Holder<List<String>> enchantsFormat;
    public EnchantLoreTag(@NotNull Holder<List<String>> enchantsFormat) {
        super(ItemStack.class);
        this.enchantsFormat = enchantsFormat;
    }

    public @NotNull Holder<List<String>> getEnchantsFormat() {
        return enchantsFormat;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.generic("cruxenchants_");
    }

    @Override
    public @Nullable Collection<LoreHook<ItemStack>> requestLore(@NotNull ItemStack object, @NotNull Tags tags) {
        return new LoreHook.Builder<>(ItemStack.class)
            .generic("enchants", (item, args, context) ->{
                List<String> format = enchantsFormat.value();
                if(format==null) return null;

                List<String> list = new ArrayList<>();
                CruxEnchant.get(item).forEach((enchant, level) ->{
                    for(String s : format){
                        list.add(
                            s.replace("<enchant_name>", enchant.getName())
                                .replace("<enchant_key>", enchant.key().asString())
                                .replace("<enchant_level>", CruxMath.numeral(level))
                                .replace("<enchant_level_raw>", level + "")
                        );
                    }
                });
                return list;
            })
            .build();
    }
}
