package killercreepr.crux.data.communication.impl;

import killercreepr.crux.Crux;
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
    public @NotNull Title build(@Nullable OfflinePlayer placeholders, @Nullable StringTagProvider tags){
        return Title.title(
            deserialize(placeholders, title, tags),
            deserialize(placeholders, subTitle, tags),
            times
        );
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
        return Crux.FORMAT.deserialize(input, StringTagProvider.mergeHook(tags, viewer));
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
