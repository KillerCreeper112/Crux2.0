package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.animation.TextAnimation;
import killercreepr.crux.core.communication.MsgContainer;
import killercreepr.crux.core.util.CruxColor;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@JsonSerializer(id = "communicator")
public class FileCommunicator extends SimpleFileHandler<Communicator> {
    public final Map<Key, FileObjectHandler<? extends Communicator>> CUSTOM_HANDLERS = new HashMap<>();
    public void registerCustomHandler(Key key, FileObjectHandler<? extends Communicator> handler){
        CUSTOM_HANDLERS.put(key, handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull Communicator object) {
        if(object instanceof MsgContainer msg){
            return BukkitCfgHandlers.MSG_CONTAINER.serializeToFile(context, msg);
        }
        return null;//todo
    }

    @Override
    public @Nullable Communicator deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        var r = ctx.getRegistry();
        if(!(e instanceof FileObject o)) return r.deserializeFromFile(MsgContainer.class, e);
        Key type = r.deserializeFromFile(Key.class, o.get("type"));
        if(type == null) return r.deserializeFromFile(MsgContainer.class, e);
        var handler = CUSTOM_HANDLERS.get(type);
        if(handler == null) throw new IllegalArgumentException("Communicator type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, e);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "communicator";
    }
}
