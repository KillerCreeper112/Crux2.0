package killercreepr.crux.tags.placeholder;

import killercreepr.crux.tags.hook.string.StringHookPlaceholder;
import org.jetbrains.annotations.NotNull;

public class LocalTag {
    public static @NotNull StringHookPlaceholder parsed(@NotNull String id, @NotNull String value){
        return StringHookPlaceholder.parsed(id, value);
    }
}
