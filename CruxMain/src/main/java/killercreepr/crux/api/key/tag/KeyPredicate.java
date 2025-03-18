package killercreepr.crux.api.key.tag;

import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.core.key.predicate.*;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public interface KeyPredicate {
    static KeyPredicate fromType(@NotNull Key type){
        return new KeyTypePredicate(type);
    }
    static KeyPredicate fromTag(@NotNull Tag<Key> tag){
        return new KeyTagPredicate(tag);
    }
    static KeyPredicate fromAllOf(@NotNull Collection<KeyPredicate> children){
        return new KeyAllPredicate(children);
    }
    static KeyPredicate fromAnyOf(@NotNull Collection<KeyPredicate> children){
        return new KeyAnyPredicate(children);
    }
    static KeyPredicate fromInverted(@NotNull KeyPredicate children){
        return new KeyInvertPredicate(children);
    }
    static KeyPredicate fromAllOf(@NotNull KeyPredicate... children){
        return new KeyAllPredicate(Arrays.asList(children));
    }
    static KeyPredicate fromAnyOf(@NotNull KeyPredicate... children){
        return new KeyAnyPredicate(Arrays.asList(children));
    }
    boolean test(@NotNull Key check);
}
