package killercreepr.crux.core.text.context;

import killercreepr.crux.api.text.context.InputContext;
import org.jetbrains.annotations.NotNull;

public class DummyInputContext implements InputContext {
    @Override
    public @NotNull String input(@NotNull String text) {
        return text;
    }
}
