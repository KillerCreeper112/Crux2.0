package killercreepr.crux.api.entity.consumer;

import killercreepr.crux.api.data.CruxKeyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface CruxEntityConsumer extends CruxKeyed {
    void accept(@NotNull Entity e);
}
