package killercreepr.cruxconfig.config.bukkit.handler.impl;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.crux.util.CruxKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FileGenericKeyedRegistry<T extends Keyed, K extends RegistryKey<T>> extends FileGenericKeyed<T> {
    protected final @NotNull RegistryKey<T> registryKey;
    public FileGenericKeyedRegistry(@NotNull RegistryKey<T> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public @Nullable T deserializeFromKey(@NotNull Key key) {
        return RegistryAccess.registryAccess().getRegistry(registryKey).get(CruxKey.key(key));
    }
}
