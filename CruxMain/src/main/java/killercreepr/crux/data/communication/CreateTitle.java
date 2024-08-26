package killercreepr.crux.data.communication;

import killercreepr.crux.data.communication.impl.SimpleCreateTitle;
import killercreepr.crux.tags.provider.StringTagProvider;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.title.Title;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public interface CreateTitle {
    static CreateTitle title(@Nullable String title, @Nullable String subTitle, @NotNull Title.Times times){
        return new SimpleCreateTitle(title, subTitle, times);
    }

    static CreateTitle title(@Nullable String title, @Nullable String subTitle){
        return title(title, subTitle, 60, 100, 60);
    }

    static CreateTitle title(@Nullable String title, @Nullable String subTitle, int fadeInTicks, int stayTicks, int fadeOutTicks){
        return title(
            title, subTitle,
            Title.Times.times(
                Duration.ofMillis(fadeInTicks * 50L),
                Duration.ofMillis(stayTicks * 50L),
                Duration.ofMillis(fadeOutTicks * 50L)
            )
        );
    }

    default CreateTitle use(@NotNull Audience a, @Nullable StringTagProvider tags){
        OfflinePlayer placeholders;
        if(a instanceof OfflinePlayer d) placeholders = d;
        else placeholders = null;
        return use(a, placeholders, tags);
    }
    default CreateTitle use(@NotNull Audience a){
        return use(a,  (StringTagProvider) null);
    }

    default CreateTitle use(@NotNull Audience a, @Nullable OfflinePlayer placeholders){
        return use(a, placeholders, null);
    }

    CreateTitle use(@NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable StringTagProvider tags);

    @NotNull Title build(@Nullable StringTagProvider tags);
    @NotNull Title build(@Nullable OfflinePlayer placeholders, @Nullable StringTagProvider tags);
    @Nullable String getTitle();

    @Nullable String getSubTitle();

    @NotNull Title.Times getTimes();
}
