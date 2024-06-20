package killercreepr.crux.tags.ab;

import killercreepr.crux.tags.ab.container.StringListTagContainer;
import org.jetbrains.annotations.NotNull;

public interface StringListTagProvider {
    @NotNull
    StringListTagContainer getStringListTags();
}
