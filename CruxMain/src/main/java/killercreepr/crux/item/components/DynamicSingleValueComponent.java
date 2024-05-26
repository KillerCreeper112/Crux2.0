package killercreepr.crux.item.components;

import killercreepr.crux.item.DynamicItemComponent;
import killercreepr.crux.util.CruxString;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class DynamicSingleValueComponent implements DynamicItemComponent {
    protected final @NotNull Object value;
    public DynamicSingleValueComponent(@NotNull Object value) {
        this.value = value;
    }

    public @NotNull Object getValue() {
        return value;
    }

    public @NotNull String parse(@NotNull InputContext context){
        return context.input(value.toString());
    }

    public @NotNull List<String> parseStringList(@NotNull InputContext context){
        if(!(value instanceof List<?> l)) return List.of();
        List<String> list = new ArrayList<>();
        for(Object s : l){
            list.add(context.input(s.toString()));
        }
        return list;
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
