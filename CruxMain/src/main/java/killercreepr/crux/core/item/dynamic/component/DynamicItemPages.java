package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DynamicItemPages extends DynamicSingleValueComponent {
    public DynamicItemPages(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "pages";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(BookMeta.class, meta ->{
            meta.addPages(
                parsePageComponentList(context).toArray(new Component[0])
            );
        });
    }

    public @NotNull List<Component> parsePageComponentList(@NotNull TextParserContext context){
        List<Object> unparsed;
        if((value instanceof List<?> l)) unparsed = (List<Object>) l;
        else if(value instanceof String s){
            unparsed = new ArrayList<>();
            unparsed.add(s);
        }else unparsed = new ArrayList<>();

        List<String> list = addPageStrings(unparsed);
        return context.deserializeList(list);
    }

    public @NotNull List<String> addPageStrings(List<?> unparsed){
        if(unparsed.isEmpty()) return List.of();
        List<String> list = new ArrayList<>();
        for(Object s : unparsed){
            String text;
            if(s instanceof List<?> d){
                text = String.join("\n", (List<String>) d);
            }else text = s.toString();
            list.add(text);
        }
        return list;
    }

    @Override
    public DynamicItemComponent clone() {
        DynamicItemPages lore = (DynamicItemPages) super.clone();
        if(lore.value instanceof List<?> l){
            return new DynamicItemPages(new ArrayList<>(l));
        }
        return lore;
    }

    /*@Override
    public @NotNull DynamicItemComponent merge(@NotNull DynamicItemComponent with) {
        if(!(with instanceof DynamicItemBook w)) return this;
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
        return new DynamicItemBook(newList);
    }*/
}
