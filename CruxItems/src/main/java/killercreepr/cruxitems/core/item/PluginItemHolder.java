package killercreepr.cruxitems.core.item;

import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import killercreepr.cruxitems.core.registries.CruxItemRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PluginItemHolder implements ItemHolder {
    protected final @NotNull Key key;
    public PluginItemHolder(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull ItemStack value() {
        PluginItem pluginItem = CruxItemRegistries.ITEMS.get(key);
        Objects.requireNonNull(pluginItem, "PluginItem is invalid! " + key);
        return pluginItem.buildItem();
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
