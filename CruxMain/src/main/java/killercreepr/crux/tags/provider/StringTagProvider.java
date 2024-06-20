package killercreepr.crux.tags.provider;

import killercreepr.crux.tags.container.StringTagContainer;
import org.jetbrains.annotations.NotNull;

public interface StringTagProvider {
    @NotNull StringTagContainer getStringTags();
}
