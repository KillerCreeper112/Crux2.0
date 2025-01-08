package killercreepr.cruxconfig.config.bukkit.handler.impl;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.communication.boss.CreateBossBar;
import killercreepr.crux.core.communication.boss.SimpleCreateBossBar;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@JsonSerializer(id = "create_boss_bar")
public class FileCreateBossBar extends SimpleFileHandler<CreateBossBar> {

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CreateBossBar title) {
        if(!(title instanceof SimpleCreateBossBar s)) throw new UnsupportedOperationException(title.getClass() + " not supported!");
        FileRegistry reg = ctx.getRegistry();
        FileObject o = new FileObject();
        String value = s.getKey();
        if(value != null) o.addProperty("key", value);

        value = s.getTitle();
        if(value != null) o.addProperty("title", value);

        value = s.getColor();
        if(value != null) o.addProperty("color", value);

        value = s.getProgress();
        if(value != null) o.addProperty("progress", value);

        value = s.getOverlay();
        if(value != null) o.addProperty("overlay", value);

        value = s.getDuration();
        if(value != null) o.addProperty("duration", value);

        Collection<String> flags = s.getFlags();
        if(flags != null) o.add("flags", reg.serializeToFile(flags));
        return o;
    }

    @Override
    public @Nullable CreateBossBar deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();
        return CreateBossBar.bossBar(
            o.getObject(String.class, "key"),
            o.getObject(String.class, "title"),
            o.getObject(String.class, "progress"),
            o.getObject(String.class, "color"),
            o.getObject(String.class, "overlay"),
            o.getObject(String.class, "duration"),
            reg.deserializeFromFile(new TypeToken<Collection<String>>(){}.getType(), o.get("flags"))
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "create_boss_bar";
    }
}
