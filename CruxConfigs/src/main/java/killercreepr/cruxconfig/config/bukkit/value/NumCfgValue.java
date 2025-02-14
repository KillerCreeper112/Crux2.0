package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.valueproviders.number.ConstantNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NumCfgValue extends NotNullValue<NumberProvider> {
    public NumCfgValue(@NotNull NumberProvider defaultValue) {
        this(defaultValue, null);
    }

    public NumCfgValue(@NotNull NumberProvider defaultValue, @NotNull String @Nullable [] comments) {
        this(defaultValue, null, comments);
    }

    public NumCfgValue(@NotNull NumberProvider defaultValue, @Nullable String path, @NotNull String @Nullable ... comments) {
        super(defaultValue, path, NumberProvider.class, comments);
    }

    public NumCfgValue(@NotNull Number defaultConstant) {
        this(defaultConstant, (String[]) null);
    }

    public NumCfgValue(@NotNull Number defaultConstant, @NotNull String @Nullable [] comments) {
        this(defaultConstant, null, comments);
    }

    public NumCfgValue(@NotNull Number defaultConstant, @Nullable String path, @NotNull String @Nullable ... comments) {
        this(new ConstantNumber(defaultConstant), path, comments);
    }

}
