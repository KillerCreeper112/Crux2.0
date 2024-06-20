package killercreepr.crux.tags.ab;

import killercreepr.crux.tags.ab.container.StringTagContainer;
import org.jetbrains.annotations.NotNull;

public interface StringTagProvider {
    @NotNull StringTagContainer getStringTags();
}
