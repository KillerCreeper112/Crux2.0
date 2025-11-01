package killercreepr.crux.core.communication.lang;

import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleCreateLang implements CreateLang {
    protected final @NotNull Map<String, Communicator> messages = new HashMap<>();

    public Collection<String> getTranslations(){
        return messages.keySet();
    }

    @Override
    public Communicator use(@NotNull String id, @NotNull Audience a, @NotNull TextParserContext ctx) {
        Communicator communicator = get(id);
        return communicator == null ? null : communicator.use(a, ctx);
    }

    @Override
    public Communicator broadcast(@NotNull String id, @Nullable MergedTagContainer tags) {
        Communicator communicator = get(id);
        return communicator == null ? null : communicator.broadcast(tags);
    }

    @Override
    public Communicator playAt(@NotNull String id, @NotNull Location at) {
        Communicator communicator = get(id);
        return communicator == null ? null : communicator.playAt(at);
    }

    @Override
    public Communicator playAt(@NotNull String id, @NotNull Entity at) {
        Communicator communicator = get(id);
        return communicator == null ? null : communicator.playAt(at);
    }

    @Override
    public @Nullable Communicator get(@NotNull String id) {
        return messages.get(id);
    }

    @Override
    public void put(@NotNull String key, @NotNull Communicator communicator) {
        messages.put(key, communicator);
    }
}
