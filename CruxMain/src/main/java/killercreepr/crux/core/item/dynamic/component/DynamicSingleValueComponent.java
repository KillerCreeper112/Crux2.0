package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxString;
import net.kyori.adventure.text.Component;
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

    public @NotNull String parseString(@NotNull TextParserContext context){
        return context.deserializeString(value.toString());
    }

    public @NotNull Component parseComponent(@NotNull TextParserContext context){
        return context.deserialize(value.toString());
    }

    public @NotNull List<Component> parseComponentList(@NotNull TextParserContext context){
        List<Object> unparsed;
        if((value instanceof List<?> l)) unparsed = (List<Object>) l;
        else if(value instanceof String s){
            unparsed = new ArrayList<>();
            unparsed.add(s);
        }else unparsed = new ArrayList<>();

        List<String> list = new ArrayList<>();
        for(Object s : unparsed){
            list.add(s.toString());
        }
        return context.deserializeList(list);
    }

    public @NotNull List<String> parseStringList(@NotNull TextParserContext context){
        if(!(value instanceof List<?> l)) return List.of();
        List<String> list = new ArrayList<>();
        for(Object s : l){
            list.add(context.deserializeString(s.toString()));
        }
        return list;
    }

    public boolean parseBool(@NotNull TextParserContext context){
        if(value instanceof Boolean b) return b;
        return CruxString.parseBoolean(parseString(context));
    }

    public double parseDouble(@NotNull TextParserContext context){
        if(value instanceof Number n) return n.doubleValue();
        if(value instanceof NumberProvider n) return n.value().doubleValue();
        return Double.parseDouble(parseString(context));
    }

    public int parseInt(@NotNull TextParserContext context){
        if(value instanceof Number n) return n.intValue();
        if(value instanceof NumberProvider n) return n.value().intValue();
        return (int) parseDouble(context);
    }
    public long parseLong(@NotNull TextParserContext context){
        if(value instanceof Number n) return n.longValue();
        if(value instanceof NumberProvider n) return n.value().longValue();
        return (long) parseDouble(context);
    }

    public float parseFloat(@NotNull TextParserContext context){
        if(value instanceof Number n) return n.floatValue();
        if(value instanceof NumberProvider n) return n.value().floatValue();
        return (float) parseDouble(context);
    }
}
