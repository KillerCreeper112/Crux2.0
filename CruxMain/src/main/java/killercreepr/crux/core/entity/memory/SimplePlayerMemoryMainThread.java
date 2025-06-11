package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.data.Holder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SimplePlayerMemoryMainThread extends SimplePlayerMemory{
    public SimplePlayerMemoryMainThread(@NotNull Player e) {
        super(e);
    }

    public SimplePlayerMemoryMainThread(@NotNull UUID uuid, @NotNull Holder<Player> player) {
        super(uuid, player);
    }

    @Override
    public boolean isAsync() {
        return false;
    }
}
