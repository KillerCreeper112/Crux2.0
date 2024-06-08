package killercreepr.cruxitems.item.plugin;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.crux.util.CruxString;
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
    public @NotNull ItemStack buildItem(@Nullable Entity holder, @Nullable StringHookContainer tags) {
        ItemStack item = buildGeneric(holder, tags);
        item.editMeta(meta ->{
            meta.displayName(Crux.FORMAT.deserialize(CruxString.toTitleCase(key.value())));
        });
        return item;
    }

    public abstract @NotNull ItemStack buildGeneric(@Nullable Entity holder, @Nullable StringHookContainer stringHookContainer);

    @Override
    public @NotNull Key key() {
        return key;
    }
}
