package killercreepr.crux.tags.provider;

import killercreepr.crux.tags.container.StringListTagContainer;
import org.jetbrains.annotations.NotNull;

public interface StringListTagProvider {
    @NotNull
    StringListTagContainer getStringListTags();
}
