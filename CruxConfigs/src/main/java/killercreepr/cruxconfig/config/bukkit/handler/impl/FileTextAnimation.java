package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.communication.animation.TextAnimation;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.util.CruxColor;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.CustomFileItemLootFunction;
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

@JsonSerializer(id = "text_animation")
public class FileTextAnimation extends SimpleFileHandler<TextAnimation> {
    public final Map<Key, FileObjectHandler<? extends TextAnimation>> CUSTOM_HANDLERS = new HashMap<>();
    public void registerCustomHandler(Key key, FileObjectHandler<? extends TextAnimation> handler){
        CUSTOM_HANDLERS.put(key, handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull TextAnimation object) {
        return null;//todo
    }

    @Override
    public @Nullable TextAnimation deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        var r = ctx.getRegistry();
        Key type = r.deserializeFromFile(Key.class, o.get("type"));
        if(type == null) return null;
        var handler = CUSTOM_HANDLERS.get(type);
        if(handler == null) throw new IllegalArgumentException("TextAnimation type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, e);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "text_animation";
    }
}
