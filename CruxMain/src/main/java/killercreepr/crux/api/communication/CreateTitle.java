package killercreepr.crux.api.communication;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.communication.SimpleCreateTitle;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.title.Title;
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
        return use(a, TextParserContext.builder().tags(TagContainer.merged(tags)).build());
    }
    default CreateTitle use(@NotNull Audience a){
        return use(a, TextParserContext.empty());
    }

    CreateTitle use(@NotNull Audience a, @NotNull TextParserContext ctx);
    @NotNull Title build(@NotNull TextParserContext ctx);
    default @NotNull Title build(@Nullable StringTagProvider tags){
        return build(tags == null ? TextParserContext.empty() : TextParserContext.builder().tags(TagContainer.merged(tags)).build());
    }
    @Nullable String getTitle();

    @Nullable String getSubTitle();

    @NotNull Title.Times getTimes();
}
