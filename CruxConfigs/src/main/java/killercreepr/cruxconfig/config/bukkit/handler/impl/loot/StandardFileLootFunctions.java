package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.loot.impl.item.functions.ItemEnchantFunction;
import killercreepr.crux.util.CruxObjects;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class StandardFileLootFunctions {
    public static void register(@NotNull FileItemLootFunction file){
        file.registerCustomHandler(new CustomFileLootFunction<>() {
            @Override
            public @NotNull String getType() {
                return "set_enchants";
            }

            @Override
            public @Nullable ItemEnchantFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                NumberProvider rolls = registry.deserializeFromFile(NumberProvider.class, e.get("rolls"));
                if(rolls==null) return null;

                if(!(e.get("enchants") instanceof FileArray oo)) return null;
                Collection<ItemEnchantFunction.Enchant> enchants = new HashSet<>();
                oo.forEach(ele ->{
                    if(!(ele instanceof FileObject f)) return;
                    int weight = f.getObject(Integer.class, "weight", 0);
                    float quality = f.getObject(Float.class, "quality", 0f);
                    Key enchantKey = registry.deserializeFromFile(Key.class, f.get("enchant"));
                    NumberProvider level = registry.deserializeFromFile(NumberProvider.class, f.get("level"));
                    if(CruxObjects.checkNull(enchantKey, level)) return;
                    enchants.add(
                        new ItemEnchantFunction.Enchant(weight, quality, enchantKey, level)
                    );
                });
                if(enchants.isEmpty()) return null;

                Collection<LootCondition> conditions = registry.deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e.get("conditions")
                );
                return new ItemEnchantFunction(
                    conditions, rolls, enchants
                );
            }
        });
    }
}
