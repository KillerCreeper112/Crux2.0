package killercreepr.crux.api.data;

import killercreepr.crux.api.math.CruxLocation;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;

public interface WorldLocation extends WorldPosition, CruxLocation {
    @ApiStatus.Experimental
    default Key worldKeyOrDefault(Key fallback){
        Key key = worldKey();
        return key == null ? fallback : key;
    }
}
