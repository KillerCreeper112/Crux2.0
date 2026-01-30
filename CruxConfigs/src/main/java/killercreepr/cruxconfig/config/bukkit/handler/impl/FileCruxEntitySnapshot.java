package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentAccessor;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileTypedDataComponent;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FileCruxEntitySnapshot extends SimpleFileHandler<CruxEntitySnapshot> {
    public final Map<Key, FileObjectHandler<? extends CruxEntitySnapshot>> CUSTOM_HANDLERS = new HashMap<>();
    public void registerCustomHandler(Key key, FileObjectHandler<? extends CruxEntitySnapshot> handler){
        CUSTOM_HANDLERS.put(key, handler);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxEntitySnapshot object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable CruxEntitySnapshot deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(e instanceof FileObject o){
            var type = context.getRegistry().deserializeFromFile(Key.class, o.get("type"));
            if(type != null){
                var handler = CUSTOM_HANDLERS.get(type);
                if(handler == null){
                    Crux.logWarning("No CruxEntitySnapshot FileHandler of " + type + "!");
                }else return handler.deserializeFromFile(context, e);
            }

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
