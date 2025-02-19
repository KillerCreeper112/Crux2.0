package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.communication.CreateTitle;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
@JsonSerializer(id = "create_title")
public class FileCreateTitle extends SimpleFileHandler<CreateTitle> {

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CreateTitle title) {
        return new FileObject()
                .addProperty("upper", title.getTitle())
                .addProperty("lower", title.getSubTitle())
                .addProperty("fade_in", title.getTimes().fadeIn().toMillis()/50L)
                .addProperty("stay", title.getTimes().stay().toMillis()/50L)
                .addProperty("fade_out", title.getTimes().fadeOut().toMillis()/50L)
                ;
    }

    @Override
    public @Nullable CreateTitle deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String title = o.getObject("upper");
        String subTitle = o.getObject("lower");
        if(title == null && subTitle == null) return null;
        return CreateTitle.title(title, subTitle,
            parseTicks(o, "fade_in"),
            parseTicks(o, "stay"),
            parseTicks(o, "fade_out")
        );
    }

    public int parseTicks(@NotNull FileObject o, @NotNull String id){
        return o.getOrDefaultObject(id, 0)*50;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "create_title";
    }
}
