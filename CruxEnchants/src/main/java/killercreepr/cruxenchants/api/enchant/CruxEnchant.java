package killercreepr.cruxenchants.api.enchant;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.DataComponentHolder;
import killercreepr.cruxenchants.core.enchant.SimpleCruxEnchant;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface CruxEnchant extends Keyed, DataComponentHolder {
    static Builder builder(Key key){
        return new SimpleCruxEnchant.Builder().key(key);
    }

    @NotNull
    Enchantment enchantment();

    ApplicableItemGroup applicableItemGroup();

    int maxLevel();
    String description();
    String displayName();
    Component displayName(int level);

    interface Builder{
        Builder key(Key key);
        Builder description(String description);
        Builder applicableItemGroup(ApplicableItemGroup group);
        Builder applicableItemTypes(ApplicableItemType... types);
        Builder editComponents(Consumer<DataComponentHandler> consumer);
        CruxEnchant build();
    }
}
