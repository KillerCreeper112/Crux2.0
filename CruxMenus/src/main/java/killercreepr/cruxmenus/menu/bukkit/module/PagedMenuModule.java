package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.crux.data.NotNullHolder;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PagedMenuModule<T> extends SimpleMenuModule{
    protected final @NotNull List<Integer> indexes;
    protected final @NotNull NotNullHolder<List<T>> values;
    protected int page = 0;
    public PagedMenuModule(@NotNull Key key, @NotNull List<Integer> indexes, @NotNull NotNullHolder<List<T>> values) {
        super(key);
        this.indexes = indexes;
        this.values = values;
    }

    public PagedMenuModule<T> addPage(int amount){
        page = CruxMath.wrap(page+amount, 0, calculateMaxPages());
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

    public @Nullable ItemStack buildPagedItem(@NotNull T value){
        return null;
    }

    public @Nullable ItemStack buildEmptyItem(){
        return null;
    }

    public int calculateMaxPages(){
        return Math.max((int) Math.ceil((double) values.value().size() / indexes.size())-1, 0);
    }

    public int convertIndexToRegistry(int invIndex, int page){
        return invIndex + (indexes.size() * page);
    }

    @Override
    public void load(@NotNull Menu menu) {
        super.load(menu);
        openPage(menu, page);
    }
}
