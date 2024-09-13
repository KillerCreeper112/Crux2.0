package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCruxBlockWrapper extends SimpleFileHandler<CruxBlockWrapper> {

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_block_wrapper";
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxBlockWrapper object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable CruxBlockWrapper deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, e);
        if(key==null) return null;
        return CruxBlockWrapper.reference(key);
    }
}
