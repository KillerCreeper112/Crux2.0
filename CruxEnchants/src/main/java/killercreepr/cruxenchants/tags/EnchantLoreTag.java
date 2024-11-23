package killercreepr.cruxenchants.tags;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringListTagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxenchants.enchant.CruxEnchant;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnchantLoreTag implements ObjectTag<ItemStack> {
    protected final @NotNull Holder<List<String>> enchantsFormat;
    public EnchantLoreTag(@NotNull Holder<List<String>> enchantsFormat) {
        this.enchantsFormat = enchantsFormat;
    }

    public @NotNull Holder<List<String>> getEnchantsFormat() {
        return enchantsFormat;
    }

    @Override
    public @NotNull Class<ItemStack> getObjectType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("cruxenchants_");
    }

    @Override
    public @Nullable StringListTagContainer requestStringLists(@NotNull ItemStack item, @NotNull TagParser tags) {
        return new StringListTagContainer(tags)
            .add(Tag.stringList("enchants", (args, context) ->{
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
            }))
            ;
    }
}
