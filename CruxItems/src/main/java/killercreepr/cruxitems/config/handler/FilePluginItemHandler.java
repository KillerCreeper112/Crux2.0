package killercreepr.cruxitems.config.handler;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxitems.item.plugin.PluginItem;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FilePluginItemHandler extends Keyed {
    @Nullable
    PluginItem deserialize(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key, @NotNull DynamicItem item);
}
