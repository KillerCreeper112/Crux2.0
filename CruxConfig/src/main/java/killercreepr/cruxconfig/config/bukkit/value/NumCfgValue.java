package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.data.NumberProviderValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.functional.EvaluationEnvironment;

import java.util.Random;

public class NumCfgValue extends CfgValue<Number> implements NumberProvider {
    protected final NumberProvider defaultValue;
    protected final ConfigValue<NumberProvider> numberValue;

    public NumCfgValue(@NotNull ConfigValue<NumberProvider> type, @NotNull String @Nullable ... comments) {
        this(null, type, comments);
    }

    public NumCfgValue(@Nullable String path, @NotNull ConfigValue<NumberProvider> type, @NotNull String @Nullable ... comments) {
        super(path, type, comments);
        defaultValue = type.getDefaultValue();
        this.numberValue = type;
    }

    public NumCfgValue(@NotNull NumberProvider typeValue, @NotNull String @Nullable ... comments) {
        this(null, new NumberProviderValue(typeValue), comments);
    }

    public NumCfgValue(@Nullable String path, @NotNull NumberProvider typeValue, @NotNull String @Nullable ... comments) {
        this(path, new NumberProviderValue(typeValue), comments);
    }

    public NumCfgValue(@NotNull Number constant, @NotNull String @Nullable ... comments) {
        this(null, new NumberProviderValue(new ConstantNumber(constant)), comments);
    }

    public NumCfgValue(@Nullable String path, @NotNull Number constant, @NotNull String @Nullable ... comments) {
        this(path, new NumberProviderValue(new ConstantNumber(constant)), comments);
    }

    @Contract("null -> null")
    public NumberProvider getOrDefaultValue(@Nullable NumberProvider defaultValue){
        NumberProvider value = numberValue.getValue();
        if(value == null) return defaultValue;
        return value;
    }

    public @NotNull NumberProvider getOrDefaultValue(){
        return getOrDefaultValue(defaultValue);
    }

    @Override
    public @NotNull Number value() {
        return getOrDefaultValue().value();
    }

    @Override
    public @NotNull Number value(@Nullable EvaluationEnvironment ev) {
        return getOrDefaultValue().value(ev);
    }

    @Override
    public @NotNull Number sample(@NotNull Random random) {
        return getOrDefaultValue().sample(random);
    }

    @Override
    public @NotNull Number getMinValue() {
        return getOrDefaultValue().getMinValue();
    }

    @Override
    public @NotNull Number getMaxValue() {
        return getOrDefaultValue().getMaxValue();
    }
}
