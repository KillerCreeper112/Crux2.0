package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCruxEntitySnapshot extends SimpleFileHandler<CruxEntitySnapshot> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxEntitySnapshot object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable CruxEntitySnapshot deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        Key key = context.getRegistry().deserializeFromFile(Key.class, e);
        if(key == null) return null;
        return Crux.handlers().entity().createEntitySnapshot(key);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_entity_snapshot";
    }
}
