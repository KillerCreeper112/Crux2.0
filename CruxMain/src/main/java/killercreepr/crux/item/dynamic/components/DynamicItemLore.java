package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.item.dynamic.DynamicItemComponent;
import killercreepr.crux.util.CruxItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DynamicItemLore extends DynamicSingleValueComponent {
    public DynamicItemLore(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "lore";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.lore(parseComponentList(context));
    }

    @Override
    public @NotNull DynamicItemComponent merge(@NotNull DynamicItemComponent with) {
        if(!(with instanceof DynamicItemLore w)) return this;
        List<Object> unparsed;
        if((value instanceof List<?> l)) unparsed = (List<Object>) l;
        else if(value instanceof String s){
            unparsed = new ArrayList<>();
            unparsed.add(s);
        }else unparsed = List.of();

        List<Object> newList = new ArrayList<>(unparsed);

        List<Object> other;
        if((w.getValue() instanceof List<?> l)) other = (List<Object>) l;
        else if(w.getValue() instanceof String s){
            other = new ArrayList<>();
            other.add(s);
        }else other = List.of();
        if(other.isEmpty()) return this;

        newList.addAll(other);
        return new DynamicItemLore(newList);
    }
}
