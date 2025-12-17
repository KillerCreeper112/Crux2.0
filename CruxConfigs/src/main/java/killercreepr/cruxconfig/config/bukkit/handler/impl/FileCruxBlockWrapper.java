package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.block.CruxBlockWrapper;
import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
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
        FileRegistry r = ctx.getRegistry();
        if(!(e instanceof FileObject o)){
            String key = r.deserializeFromFile(String.class, e);
            if(key==null) return null;
            return CruxBlockWrapper.reference(key);
        }

        String key = r.deserializeFromFile(String.class, o.get("type"));
        if(key==null) return null;
        return CruxBlockWrapper.reference(
            key,
            r.deserializeFromFile(DataComponentAccessor.class, o.get("components"))
        );
    }
}
