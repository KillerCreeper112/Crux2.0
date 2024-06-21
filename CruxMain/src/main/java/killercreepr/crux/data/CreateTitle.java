package killercreepr.crux.data;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.provider.StringTagProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateTitle {
    protected final String title;
    protected final String subTitle;
    protected final Title.Times times;

    public CreateTitle(@Nullable String title, @Nullable String subTitle, @NotNull Title.Times times) {
        this.title = title;
        this.subTitle = subTitle;
        this.times = times;
    }

    public @NotNull Title build(@Nullable OfflinePlayer placeholders, @Nullable StringTagProvider tags){
        return Title.title(
                deserialize(placeholders, title, tags),
                deserialize(placeholders, subTitle, tags),
                times
        );
    }

    public @NotNull Title build(@Nullable StringTagProvider tags){
        return build(null, tags);
    }

    protected @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @Nullable String input, @Nullable StringTagProvider tags){
        if(input == null) return Component.empty();//todo include viewer
        return Crux.FORMAT.deserialize(input, tags);
    }

    public @Nullable String getTitle() {
        return title;
    }

    public @Nullable String getSubTitle() {
        return subTitle;
    }

    public @NotNull Title.Times getTimes() {
        return times;
    }
}
