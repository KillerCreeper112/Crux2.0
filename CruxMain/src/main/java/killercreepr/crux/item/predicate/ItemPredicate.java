package killercreepr.crux.item.predicate;

import killercreepr.crux.Tag;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public interface ItemPredicate {
    static ItemPredicate fromType(@NotNull Key type){
        return new ItemTypePredicate(type);
    }
    static ItemPredicate fromTag(@NotNull Tag<ItemStack> tag){
        return new ItemTagPredicate(tag);
    }
    static ItemPredicate fromAllOf(@NotNull Collection<ItemPredicate> children){
        return new ItemAllPredicate(children);
    }
    static ItemPredicate fromAllOf(@NotNull ItemPredicate... children){
        return new ItemAllPredicate(Arrays.asList(children));
    }
    boolean test(@NotNull ItemStack item);
}
