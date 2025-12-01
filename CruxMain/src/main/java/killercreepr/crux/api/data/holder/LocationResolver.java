package killercreepr.crux.api.data.holder;

import killercreepr.crux.api.text.context.InputContext;
import org.bukkit.Location;

public interface LocationResolver {
    Location resolveLocation(InputContext ctx);
}
