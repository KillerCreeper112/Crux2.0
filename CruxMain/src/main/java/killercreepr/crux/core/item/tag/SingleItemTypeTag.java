package killercreepr.crux.core.item.tag;

import killercreepr.crux.api.item.ItemListHolder;
import killercreepr.crux.core.Crux;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class SingleItemTypeTag extends BaseItemTag implements ItemListHolder {
    protected final @NotNull Key value;
    public SingleItemTypeTag(@NotNull Key key, @NotNull Key value) {
        super(key);
        this.value = value;
    }

    @Override
    public boolean isTagged(@NotNull ItemStack item) {
        return value.equals(Crux.handlers().item().getType(item));
    }

    @Deprecated(forRemoval = true)
    public @NotNull Collection<ItemStack> getValues() {
        Collection<ItemStack> list = new HashSet<>();
        ItemHolder holder = Crux.handlers().item().getItem(value);
        if(holder==null) return list;
        list.add(holder.value());
        return list;
    }

    public @NotNull Key getType() {
        return value;
    }

    @Override
    public @NotNull List<ItemStack> getItemValues() {
        List<ItemStack> list = new ArrayList<>();
        ItemHolder holder = Crux.handlers().item().getItem(value);
        if(holder==null) return list;
        list.add(holder.value());
        return list;
    }
}
