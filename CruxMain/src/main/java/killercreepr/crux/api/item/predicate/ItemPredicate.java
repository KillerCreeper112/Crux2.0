package killercreepr.crux.api.item.predicate;

import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.core.item.predicate.ItemAllPredicate;
import killercreepr.crux.core.item.predicate.ItemInvertPredicate;
import killercreepr.crux.core.item.predicate.ItemTagPredicate;
import killercreepr.crux.core.item.predicate.ItemTypePredicate;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public interface ItemPredicate extends Predicate<ItemStack> {
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

    static ItemPredicate fromInverted(@NotNull ItemPredicate predicate){
        return new ItemInvertPredicate(predicate);
    }

    boolean test(@NotNull ItemStack item);
}
