package killercreepr.cruxstructures.config.module;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.module.standard.CorruptedVeinModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCorruptedVeinModule extends PureYamlFileHandler<CorruptedVeinModule> {
    @Override
    public @Nullable CorruptedVeinModule deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        return new CorruptedVeinModule(
            registry.deserializeFromFile(NumberProvider.class, o.get("vein_length")),
            registry.deserializeFromFile(NumberProvider.class, o.get("vein_rotate")),
            registry.deserializeFromFile(NumberProvider.class, o.get("vein_rotate_y")),
            registry.deserializeFromFile(Key.class, o.get("vein_block")),
            registry.deserializeFromFile(NumberVector.class, o.get("offset")),
            registry.deserializeFromFile(BlockPredicate.class, o.get("replaceable"))
        );
    }
}
