package killercreepr.crux.data;

import killerceepr.crux.Crux;
import killerceepr.crux.tags.container.StringHookContainer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
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

    public @NotNull Title build(@NotNull Player p, @Nullable StringHookContainer tags){
        return Title.title(
                Crux.FORMAT.deserialize(p, null, title == null ? "" : title, tags),
                Crux.FORMAT.deserialize(p, null, subTitle == null ? "" : subTitle, tags),
                times
        );
    }

    public @NotNull Title build(@Nullable StringHookContainer tags){
        return Title.title(
                Crux.FORMAT.deserialize(title == null ? "" : title, tags),
                Crux.FORMAT.deserialize(subTitle == null ? "" : subTitle, tags),
                times
        );
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
