package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlGenericKeyedRegistry<T extends Keyed, K extends RegistryKey<T>> extends YamlGenericKeyed<T>{
    protected final @NotNull RegistryKey<T> registryKey;
    public YamlGenericKeyedRegistry(@NotNull RegistryKey<T> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public @Nullable T deserializeFromKey(@NotNull NamespacedKey key) {
        return RegistryAccess.registryAccess().getRegistry(registryKey).get(key);
    }
}
