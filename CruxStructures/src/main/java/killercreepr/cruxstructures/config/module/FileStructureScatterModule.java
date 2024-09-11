package killercreepr.cruxstructures.config.module;

import killercreepr.crux.loot.LootTable;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.CruxStructuresModule;
import killercreepr.cruxstructures.structure.module.standard.StructureScatterModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileStructureScatterModule extends PureYamlFileHandler<StructureScatterModule> {
    protected static final @NotNull FileSimpleLootTable<Key> fileSimpleLootTable = CruxStructuresModule.fileSimpleLootTable;
    @Override
    public @Nullable StructureScatterModule deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        LootTable<Key> structures = fileSimpleLootTable.deserializeFromFile(
            ctx, o.get("structures")
        );
        if(structures == null) return null;
        NumberProvider maxAttempts = registry.deserializeFromFile(NumberProvider.class, o.get("max_attempts"));
        if(maxAttempts == null) maxAttempts = NumberProvider.constant(16);

        NumberProvider range = registry.deserializeFromFile(NumberProvider.class, o.get("range"));
        if(range==null) return null;
        NumberProvider yRange = registry.deserializeFromFile(NumberProvider.class, o.get("range_y"));
        if(yRange == null) yRange = range;

        return new StructureScatterModule(
            structures,
            range, yRange,
            maxAttempts
        );
    }
}
