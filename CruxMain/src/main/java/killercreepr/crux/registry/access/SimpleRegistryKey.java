package killercreepr.crux.registry.access;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public record SimpleRegistryKey<T>(@NotNull Key key) implements RegistryKey<T> {

}
