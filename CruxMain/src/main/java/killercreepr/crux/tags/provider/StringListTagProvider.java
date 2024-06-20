package killercreepr.crux.tags.provider;

import killercreepr.crux.tags.container.StringListTagContainer;
import killercreepr.crux.tags.container.StringTagContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StringListTagProvider {
    @Contract("null -> null")
    static @Nullable StringListTagProvider build(@Nullable StringListTagContainer tags){
        if(tags==null) return null;
        return () -> tags;
    }
    @NotNull
    StringListTagContainer getStringListTags();
}
