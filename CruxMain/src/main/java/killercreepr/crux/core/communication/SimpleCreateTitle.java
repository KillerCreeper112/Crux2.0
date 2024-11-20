package killercreepr.crux.core.communication;

import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.communication.CreateTitle;
import killercreepr.crux.tags.provider.StringTagProvider;
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
    public @NotNull Title build(@Nullable OfflinePlayer placeholders, @Nullable StringTagProvider tags){
        return Title.title(
            deserialize(placeholders, title, tags),
            deserialize(placeholders, subTitle, tags),
            times
        );
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

    @Override
    public CreateTitle use(@NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable StringTagProvider tags) {
        a.showTitle(build(placeholders, tags));
        return this;
    }
    @Override
    public @NotNull Title build(@Nullable StringTagProvider tags){
        return build(null, tags);
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
