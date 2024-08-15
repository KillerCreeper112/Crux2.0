package killercreepr.cruxstructures.config.structure;

import killercreepr.cruxconfig.config.bukkit.handler.YamlFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.impl.StructureBiomeRequirement;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileBiomeRequirement extends YamlFileHandler<StructureBiomeRequirement> {
    @Override
    public @Nullable StructureBiomeRequirement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return new StructureBiomeRequirement(key);
    }

}
