package killercreepr.cruxitems.config.handler;

import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxitems.item.plugin.PluginItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PluginItemHandler implements FileHandler<PluginItem> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull PluginItem object) {
        return null;
    }

    @Override
    public @Nullable PluginItem deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return null;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "";
    }
}
