package killercreepr.cruxconfig.config.bukkit.handler.impl;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "particle")
public class FileParticle extends SimpleFileHandler<Particle> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull Particle object) {
        return new FilePrimitive(object.key().asString());
    }

    @Override
    public @Nullable Particle deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive s) || !s.isString()) return null;
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.PARTICLE_TYPE).get(Key.key(s.getAsString()));
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "particle";
    }
}
