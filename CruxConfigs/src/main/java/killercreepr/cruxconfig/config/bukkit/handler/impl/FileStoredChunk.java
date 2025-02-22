package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.data.world.StoredChunk;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "stored_chunk")
public class FileStoredChunk implements FileObjectHandler<StoredChunk> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull StoredChunk obj) {
        return new FileObject()
            .add("world", ctx.getRegistry().serializeToFile(obj.worldKey()))
            .addProperty("chunk_x", obj.chunkX())
            .addProperty("chunk_z", obj.chunkZ())
            ;
    }

    @Override
    public @Nullable StoredChunk deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        Key key = ctx.getRegistry().deserializeFromFile(Key.class, o.get("world"));
        if(key == null) return null;
        Integer chunkX = o.getObject(Integer.class, "chunk_x");
        Integer chunkZ = o.getObject(Integer.class, "chunk_z");
        if(chunkX == null || chunkZ == null) return null;
        return StoredChunk.storedChunk(key, chunkX, chunkZ);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "stored_chunk";
    }
}
