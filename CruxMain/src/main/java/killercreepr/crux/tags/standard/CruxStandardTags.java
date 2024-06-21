package killercreepr.crux.tags.standard;

import killercreepr.crux.tags.hook.ObjectTag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class CruxStandardTags {
    public static @NotNull Collection<ObjectTag<?>> build(){
        return new HashSet<>(){{
            add(new OfflinePlayerTags());
        }};
    }
}
