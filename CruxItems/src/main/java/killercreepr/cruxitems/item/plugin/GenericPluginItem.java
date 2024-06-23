package killercreepr.cruxitems.item.plugin;

import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GenericPluginItem implements PluginItem{
    protected final @NotNull Key key;
    public GenericPluginItem(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull ItemStack buildItem(@Nullable Entity holder, @Nullable MergedTagContainer tags) {
        return build(holder, tags).setPluginItem(key()).item();
    }
}
