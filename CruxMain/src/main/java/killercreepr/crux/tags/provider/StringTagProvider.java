package killercreepr.crux.tags.provider;

import killercreepr.crux.tags.container.StringTagContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StringTagProvider {
    @Contract("null -> null")
    static @Nullable StringTagProvider build(@Nullable StringTagContainer tags){
        if(tags==null) return null;
        return () -> tags;
    }
    @NotNull StringTagContainer getStringTags();
}
