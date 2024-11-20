package killercreepr.crux.core.communication;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.communication.CreateTitle;
import killercreepr.crux.api.text.provider.StringTagProvider;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleCreateTitle implements CreateTitle {
    protected final String title;
    protected final String subTitle;
    protected final Title.Times times;

    public SimpleCreateTitle(@Nullable String title, @Nullable String subTitle, @NotNull Title.Times times) {
        this.title = title;
        this.subTitle = subTitle;
        this.times = times;
    }

    @Override
    public String toString() {
        return "SimpleCreateTitle{title=" + title + ", subTitle=" + subTitle + ", times=" + times + "}";
    }

    @Override
    public @NotNull Title build(@NotNull TextParserContext ctx) {
        return Title.title(
            deserialize(title, ctx),
            deserialize(subTitle, ctx),
            times
        );
    }

    @Override
    public CreateTitle use(@NotNull Audience a, @NotNull TextParserContext ctx) {
        a.showTitle(build(ctx));
        return this;
    }
    protected @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @Nullable String input, @Nullable StringTagProvider tags){
        if(input == null) return Component.empty();
        return Crux.format().deserialize(input, StringTagProvider.mergeHook(tags, viewer));
    }

    protected @NotNull Component deserialize(@Nullable String input, @NotNull TextParserContext ctx){
        if(input == null) return Component.empty();
        return ctx.deserialize(input);
    }
    @Override
    public @Nullable String getTitle() {
        return title;
    }
    @Override
    public @Nullable String getSubTitle() {
        return subTitle;
    }
    @Override
    public @NotNull Title.Times getTimes() {
        return times;
    }
}
