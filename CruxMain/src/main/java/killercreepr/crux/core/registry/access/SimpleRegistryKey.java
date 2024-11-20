package killercreepr.crux.core.registry.access;

import killercreepr.crux.api.registry.access.RegistryKey;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public record SimpleRegistryKey<T>(@NotNull Key key) implements RegistryKey<T> {

}
