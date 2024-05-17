package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.data.CreateTitle;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.ConfigValue;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class CreateTitleValue extends ConfigValue<CreateTitle> {
    public CreateTitleValue(@Nullable CreateTitle defaultValue) {
        super(CreateTitle.class, defaultValue);
    }

    public CreateTitleValue() {
        super(CreateTitle.class);
    }

    @Override
    public @Nullable CreateTitle get(@NotNull CruxConfig cfg, @NotNull String path) {
        String upper = cfg.config().getString(path + "upper");
        String lower = cfg.config().getString(path + "lower");
        if(upper == null && lower == null) return null;
        return new CreateTitle(upper, lower,
                Title.Times.times(Duration.ofMillis(cfg.config().getInt(path + "fade_in")*50L),
                        Duration.ofMillis(cfg.config().getInt(path + "stay")*50L),
                        Duration.ofMillis(cfg.config().getInt(path + "fade_out")*50L)));
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable CreateTitle title) {
        if(title == null){
            cfg.set(removeDot(path), null);
            return;
        }
        cfg.set(path + "upper", title.getTitle());
        cfg.set(path + "lower", title.getSubTitle());
        cfg.set(path + "fade_in", title.getTimes().fadeIn().toMillis()/50L);
        cfg.set(path + "stay", title.getTimes().stay().toMillis()/50L);
        cfg.set(path + "fade_out", title.getTimes().fadeOut().toMillis()/50L);
    }
}
