package killercreepr.crux.context;

import org.jetbrains.annotations.NotNull;

public class DummyInputContext implements InputContext{
    @Override
    public @NotNull String input(@NotNull String text) {
        return text;
    }
}
