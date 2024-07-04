package killercreepr.crux.data.communication;

import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SimpleCreateLang implements CreateLang {
    protected final @NotNull Map<String, Communicator> messages = new HashMap<>();
    @Override
    public Communicator use(@NotNull String id, @NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags) {
        Communicator communicator = get(id);
        return communicator == null ? null : communicator.use(a, placeholders, tags);
    }

    @Override
    public Communicator broadcast(@NotNull String id, @Nullable MergedTagContainer tags) {
        Communicator communicator = get(id);
        return communicator == null ? null : communicator.broadcast(tags);
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
