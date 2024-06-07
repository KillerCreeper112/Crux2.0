package killercreepr.crux.data;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Nullable;

public interface CruxKeyed extends Keyed {
    default boolean compare(@Nullable CruxKeyed with){
        if(with==null) return false;
        return compare(with.key());
    }
    default boolean compare(@Nullable Key with){
        return key().equals(with);
    }
}
