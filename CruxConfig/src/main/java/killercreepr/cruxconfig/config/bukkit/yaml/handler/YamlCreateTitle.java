package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.data.CreateTitle;
import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class YamlCreateTitle implements YamlObjectHandler<CreateTitle> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull CreateTitle title) {
        return new YamlObject()
                .addProperty("upper", title.getTitle())
                .addProperty("lower", title.getSubTitle())
                .addProperty("fade_in", title.getTimes().fadeIn().toMillis()/50L)
                .addProperty("stay", title.getTimes().stay().toMillis()/50L)
                .addProperty("fade_out", title.getTimes().fadeOut().toMillis()/50L)
                ;
    }

    @Override
    public @Nullable CreateTitle deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlObject o)) return null;
        String title = o.getObject("upper");
        String subTitle = o.getObject("lower");
        if(title == null && subTitle == null) return null;
        return new CreateTitle(title, subTitle,
                Title.Times.times(
                        parseTicks(o, "fade_in"),
                        parseTicks(o, "stay"),
                        parseTicks(o, "fade_out")
                ));
    }

    public Duration parseTicks(@NotNull YamlObject o, @NotNull String id){
        return Duration.ofMillis(o.getOrDefaultObject(id, 0)*50L);
    }
}
