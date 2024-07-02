package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.data.NotNullHolder;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.module.SimpleMenuModule;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PagedMenuModule<T> extends SimpleMenuModule {
    protected final @NotNull List<Integer> indexes;
    protected final @NotNull NotNullHolder<List<T>> values;
    protected int page = 0;
    public PagedMenuModule(@NotNull Key key, @NotNull String id, @NotNull List<Integer> indexes, @NotNull NotNullHolder<List<T>> values) {
        super(key, id);
        this.indexes = indexes;
        this.values = values;
    }

    public PagedMenuModule<T> addPage(int amount){
        return setPage(page+amount);
    }

    public PagedMenuModule<T> setPage(int amount){
        page = CruxMath.wrap(amount, 0, calculateMaxPages());
        return this;
    }


    public void openPage(@NotNull Menu menu, int page){
        page = CruxMath.clamp(page, 0, calculateMaxPages());
        List<T> list = values.value();
        for(int i : indexes){
            int valueIndex = convertIndexToRegistry(i, page);
            ItemStack item;
            if(valueIndex >= list.size()) item = buildEmptyItem();
            else item = buildPagedItem(list.get(i));
            menu.setItem(i, item);
        }
    }

    public abstract @Nullable ItemStack buildPagedItem(@NotNull T value);
    public abstract @Nullable ItemStack buildEmptyItem();

    public int calculateMaxPages(){
        return Math.max((int) Math.ceil((double) values.value().size() / indexes.size())-1, 0);
    }

    public int convertIndexToRegistry(int invIndex, int page){
        return invIndex + (indexes.size() * page);
    }

    @Override
    public void reload(@NotNull Menu menu) {
        super.reload(menu);
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
