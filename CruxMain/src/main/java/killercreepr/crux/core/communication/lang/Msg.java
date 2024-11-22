package killercreepr.crux.core.communication.lang;

import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Msg extends TranslateMsg implements Communicator {
    protected final @NotNull Holder<CreateLang> langHolder;
    public Msg(@NotNull String id, @NotNull Holder<CreateLang> langHolder) {
        super(id);
        this.langHolder = langHolder;
    }

    public Msg(@NotNull String id, @Nullable Communicator defaultValue, @NotNull Holder<CreateLang> langHolder) {
        super(id, defaultValue);
        this.langHolder = langHolder;
    }

    @ApiStatus.Experimental
    public Msg(@Nullable Communicator defaultValue, @NotNull Holder<CreateLang> langHolder) {
        super(defaultValue);
        this.langHolder = langHolder;
    }

    @Override
    public Communicator use(@NotNull Audience a, @NotNull TextParserContext ctx) {
        return langHolder.value().use(id, a, ctx);
    }

    @Override
    public Communicator broadcast(@Nullable MergedTagContainer mergedTagContainer) {
        return langHolder.value().broadcast(id, mergedTagContainer);
    }

    @Override
    public Communicator broadcast(@NotNull TextParserContext ctx) {
        return null;
    }

    @Override
    public Communicator playAt(@NotNull Location location) {
        return langHolder.value().playAt(id, location);
    }

    @Override
    public Communicator playAt(@NotNull Entity entity) {
        return langHolder.value().playAt(id, entity);
    }

    @Override
    public String toString() {
        return "Msg={id=" + id + ", defaultValue=" + defaultValue + ", langHolder=" + langHolder + ", langHolderValue=" + langHolder.value().get(id) + "}";
    }
}