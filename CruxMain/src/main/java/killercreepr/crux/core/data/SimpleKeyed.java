package killercreepr.crux.core.data;

import killercreepr.crux.api.data.CruxKeyed;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleKeyed implements CruxKeyed {
    protected final Key key;

    public SimpleKeyed(Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
