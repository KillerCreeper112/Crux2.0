package killercreepr.crux.api.communication.boss;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.communication.boss.SimpleCreateBossBar;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CreateBossBar {
    static CreateBossBar bossBar(@Nullable String key, @Nullable String title,
                                 @Nullable String progress,
                                 @Nullable String color, @Nullable String style,
                                 @Nullable String duration, @Nullable Collection<String> flags){
        return new SimpleCreateBossBar(key, title, progress, color, style, duration, flags);
    }

    static CreateBossBar.Builder builder(){
        return new SimpleCreateBossBar.Builder();
    }

    default @Nullable ActiveBossBar showBossBar(@NotNull Audience audience){
        return showBossBar(audience, TextParserContext.empty());
    }
    default @Nullable ActiveBossBar hideBossBar(@NotNull Audience audience){
        return hideBossBar(audience, TextParserContext.empty());
    }

    @Nullable ActiveBossBar hideBossBar(@NotNull Audience audience, @NotNull TextParserContext ctx);
    @Nullable ActiveBossBar showBossBar(@NotNull Audience audience, @NotNull TextParserContext ctx);

    @NotNull
    ActiveBossBar build(@NotNull TextParserContext ctx);
    default @NotNull ActiveBossBar build(@Nullable StringTagProvider tags){
        return build(TextParserContext.builder().tags(TagContainer.merged(tags)).build());
    }

    @NotNull
    BossBar updateBossBar(@NotNull BossBar bar, @NotNull TextParserContext ctx);

    interface Builder{
        Builder key(@Nullable String key);

        Builder title(@Nullable String title);

        Builder progress(@Nullable String progress);

        Builder color(@Nullable String color);

        Builder style(@Nullable String style);

        Builder duration(@Nullable String duration);

        Builder flags(@Nullable Collection<String> flags);

        CreateBossBar build();
    }
}
