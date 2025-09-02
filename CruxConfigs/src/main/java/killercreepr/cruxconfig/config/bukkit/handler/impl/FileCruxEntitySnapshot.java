package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
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
        if(e instanceof FileObject o){
            Key key = context.getRegistry().deserializeFromFile(Key.class, o.get("entity_type"));
            if(key == null) return null;

            return Crux.handlers().entity().createEntitySnapshot(
                key,
                context.getRegistry().deserializeFromFile(DataComponentAccessor.class, o.get("components"))
            );
        }

        Key key = context.getRegistry().deserializeFromFile(Key.class, e);
        if(key == null) return null;

        return Crux.handlers().entity().createEntitySnapshot(key, null);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_entity_snapshot";
    }
}
