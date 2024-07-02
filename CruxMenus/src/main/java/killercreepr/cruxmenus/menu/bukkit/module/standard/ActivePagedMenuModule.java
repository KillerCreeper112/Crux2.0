package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.data.NotNullHolder;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import killercreepr.cruxmenus.menu.bukkit.module.SimpleActiveMenuModule;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ActivePagedMenuModule<T> extends SimpleActiveMenuModule {
    protected final @NotNull List<Integer> indexes;
    protected final @NotNull NotNullHolder<List<T>> values;
    protected int page = 0;
    public ActivePagedMenuModule(@NotNull String id,
                                 @NotNull MenuModule module,
                                 @NotNull List<Integer> indexes,
                                 @NotNull NotNullHolder<List<T>> values) {
        super(id, module);
        this.indexes = indexes;
        this.values = values;
    }

    public ActivePagedMenuModule<T> addPage(int amount){
        return setPage(page+amount);
    }

    public ActivePagedMenuModule<T> setPage(int amount){
        page = CruxMath.wrap(amount, 0, calculateMaxPages());
        return this;
    }

    @Override
    public @Nullable MergedTagContainer buildTags(@NotNull Menu menu, @NotNull TagParser tagParser) {
        MergedTagContainer tags = new MultiTagContainer(tagParser);
        tags.addAll(super.buildTags(menu, tagParser));
        tags.add(Tag.parsed("module_" + id() + "_page", page+""));
        return tags;
    }

    public void openPage(@NotNull Menu menu, int page){
        page = CruxMath.clamp(page, 0, calculateMaxPages());
        List<T> list = values.value();
        int index = -1;
        int addon = indexes.size() * page;
        for(int i : indexes){
            index++;
            int valueIndex = index + addon;
            ItemStack item;
            if(valueIndex >= list.size()) item = buildEmptyItem(menu);
            else item = buildPagedItem(menu, list.get(valueIndex));
            menu.setItem(i, item);
        }
    }

    public abstract @Nullable ItemStack buildPagedItem(@NotNull Menu menu, @NotNull T value);
    public abstract @Nullable ItemStack buildEmptyItem(@NotNull Menu menu);

    public int calculateMaxPages(){
        return Math.max((int) Math.ceil((double) values.value().size() / indexes.size())-1, 0);
    }

    @Override
    public void refresh(@NotNull Menu menu) {
        super.refresh(menu);
        openPage(menu, page);
    }

    public @NotNull List<Integer> getIndexes() {
        return indexes;
    }

    public @NotNull NotNullHolder<List<T>> getValues() {
        return values;
    }

    public int getPage() {
        return page;
    }
}
