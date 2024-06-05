package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NumCfgValue extends NotNullValue<NumberProvider> {
    public NumCfgValue(@NotNull NumberProvider defaultValue) {
        super(defaultValue);
    }

    public NumCfgValue(@NotNull NumberProvider defaultValue, @NotNull String @Nullable [] comments) {
        super(defaultValue, comments);
    }

    public NumCfgValue(@NotNull NumberProvider defaultValue, @Nullable String path, @NotNull String @Nullable ... comments) {
        super(defaultValue, path, comments);
    }

    public NumCfgValue(@NotNull Number defaultConstant) {
        this(defaultConstant, (String[]) null);
    }

    public NumCfgValue(@NotNull Number defaultConstant, @NotNull String @Nullable [] comments) {
        this(defaultConstant, null, comments);
    }

    public NumCfgValue(@NotNull Number defaultConstant, @Nullable String path, @NotNull String @Nullable ... comments) {
        super(new ConstantNumber(defaultConstant), path, comments);
    }
}
