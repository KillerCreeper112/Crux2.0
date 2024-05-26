package killercreepr.crux.item.components;

import killercreepr.crux.item.DynamicItemComponent;
import killercreepr.crux.util.CruxString;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;

public abstract class DynamicSingleValueComponent implements DynamicItemComponent {
    protected final @NotNull String value;
    public DynamicSingleValueComponent(@NotNull String value) {
        this.value = value;
    }

    public @NotNull String getValue() {
        return value;
    }

    public @NotNull String parse(@NotNull InputContext context){
        return context.input(value);
    }

    public boolean parseBool(@NotNull InputContext context){
        return CruxString.parseBoolean(parse(context));
    }

    public double parseDouble(@NotNull InputContext context){
        return Double.parseDouble(parse(context));
    }

    public int parseInt(@NotNull InputContext context){
        return (int) parseDouble(context);
    }
    public long parseLong(@NotNull InputContext context){
        return (long) parseDouble(context);
    }

    public float parseFloat(@NotNull InputContext context){
        return (float) parseDouble(context);
    }
}
