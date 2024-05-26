package killercreepr.crux.tags;

import org.jetbrains.annotations.NotNull;

public class FormatArgs {
    public static @NotNull FormatArgs empty(){
        return new FormatArgs(new String[0]);
    }

    protected final @NotNull String[] args;
    public FormatArgs(@NotNull String[] args) {
        this.args = args;
    }

    public @NotNull String[] getArgs() {
        return args;
    }
}
