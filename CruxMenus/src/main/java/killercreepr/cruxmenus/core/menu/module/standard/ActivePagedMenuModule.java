package killercreepr.cruxmenus.core.menu.module.standard;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.module.active.IActivePagedMenuModule;
import killercreepr.cruxmenus.core.menu.module.SimpleActiveMenuModuled;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ActivePagedMenuModule<T> extends SimpleActiveMenuModuled implements IActivePagedMenuModule<T> {
    protected final @NotNull NumberProvider indexes;
    protected final @Nullable String valueFilter;
    protected final @NotNull Holder<List<T>> values;
    protected int page = 0;
    public ActivePagedMenuModule(@NotNull String id,
                                 @NotNull MenuModule module,
                                 @NotNull NumberProvider indexes, @Nullable String valueFilter,
                                 @NotNull Holder<List<T>> values) {
        super(id, module);
        this.indexes = indexes;
        this.valueFilter = valueFilter;
        this.values = values;
    }

    public @NotNull List<T> buildValues(@NotNull TextParserContext ctx){
        List<T> value = new ArrayList<>(values.value());
        return filterValues(value, ctx);
    }

    public @NotNull List<T> filterValues(@NotNull List<T> value, @NotNull TextParserContext ctx){
        if(valueFilter == null) return value;
        return value.stream().filter(v -> CruxString.parseBoolean(
            CruxMath.evaluateEvalEx(ctx.deserializeString(valueFilter, buildTags(v)))
        )).toList();
    }

    public abstract MergedTagContainer buildTags(T value);

    public ActivePagedMenuModule<T> addPage(int amount){
        return setPage(page+amount);
    }

    public ActivePagedMenuModule<T> setPage(int amount){
        page = CruxMath.wrap(amount, 0, calculateMaxPages());
        return this;
    }

    @Override
    public @Nullable MergedTagContainer buildTags(@NotNull Menu menu, @NotNull TagParser tagParser) {
        MergedTagContainer tags = TagContainer.merged(tagParser);
        tags.addAll(super.buildTags(menu, tagParser));
        String prefix = MenuModule.buildTagPrefix(id);
        tags.add(Tag.parsed(prefix + "page", page+""));
        tags.add(Tag.parsed(prefix + "max_page", calculateMaxPages()+""));
        return tags;
    }

    public TextParserContext buildContext(@NotNull Menu menu){
        return TextParserContext.builder()
            .tags(buildTags(menu, Crux.tags()))
            .build();
    }

    public void openPage(@NotNull Menu menu, int page){
        List<T> list = buildValues(buildContext(menu));
        page = CruxMath.clamp(page, 0, calculateMaxPages(list.size()));
        int index = -1;
        List<Number> indexes = this.indexes.sampleList();
        int addon = indexes.size() * page;
        for(Number number : indexes){
            int i = number.intValue();
            index++;
            int listIndex = index + addon;
            if(listIndex >= list.size()) setEmptyItem(menu, i, index);
            else setPagedItem(menu, i, index, listIndex, list.get(listIndex));
        }
    }

    public abstract void setPagedItem(@NotNull Menu menu, int slot, int index,int listIndex, @NotNull T value);
    public abstract void setEmptyItem(@NotNull Menu menu, int slot, int index);

    public int calculateMaxPages(){
        return calculateMaxPages(values.value().size());
    }

    public int calculateMaxPages(int valuesSize){
        List<Number> indexes = this.indexes.sampleList();
        return Math.max((int) Math.ceil((double) valuesSize / indexes.size())-1, 0);
    }

    @Override
    public void refresh(@NotNull Menu menu) {
        super.refresh(menu);
        openPage(menu, page);
    }

    public @NotNull NumberProvider getIndexes() {
        return indexes;
    }

    public @NotNull Holder<List<T>> getValues() {
        return values;
    }

    public int getPage() {
        return page;
    }
}
